package com.csi.palabakosys.app.joborders.create.delivery

import androidx.lifecycle.*
import com.csi.palabakosys.model.EnumDeliveryOption
import com.csi.palabakosys.model.Rule
import com.csi.palabakosys.room.repository.DeliveryProfilesRepository
import com.csi.palabakosys.util.DataState
import com.csi.palabakosys.util.InputValidation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.*
import javax.inject.Inject

@HiltViewModel
class DeliveryViewModel

@Inject
constructor(
    private val repository: DeliveryProfilesRepository
) : ViewModel()
{
    var deliveryProfiles = MutableLiveData<List<MenuDeliveryProfile>>()
    val deliveryOption = MutableLiveData(EnumDeliveryOption.PICKUP_AND_DELIVERY)
    val distance = MutableLiveData("1")
    val profile = MutableLiveData<MenuDeliveryProfile>()

    private val _dataState = MutableLiveData<DataState<DeliveryCharge>>()
    val dataState: LiveData<DataState<DeliveryCharge>> = _dataState

    val label = MediatorLiveData<String>().apply {
        fun update() {
            value = profile.value?.vehicle.toString()
        }
        addSource(profile) {update()}
    }

    fun resetState() {
        _dataState.value = DataState.StateLess
    }

    fun setDeliveryProfile(profileId: UUID) {
        this.profile.value = deliveryProfiles.value?.find { it.deliveryProfileRefId == profileId }
    }

    fun prepareDeliveryCharge(delete: Boolean) {
        val inputValidation = InputValidation()

        val option = this.deliveryOption.value
        val profile = this.profile.value

        inputValidation.addRules("distance", distance.value,
            arrayOf(
                Rule.Required,
                Rule.IsNumeric(distance.value))
        )

        if(profile == null) {
            inputValidation.addError("profile", "Please select delivery profile")
        }

        if(option == null) {
            inputValidation.addError("option", "Please select delivery option")
        }

        if(inputValidation.isInvalid()) {
            _dataState.value = DataState.InvalidInput(inputValidation)
            return
        }

        val distance = this.distance.value?.toFloat()
        val price = option!!.charge * ((profile!!.pricePerKm * distance!!) + profile.baseFare)
        val deletedAt = if(delete) { Instant.now() } else { null }

        val deliveryCharge = DeliveryCharge(profile.deliveryProfileRefId, profile.vehicle, distance, option, price, deletedAt)
        this._dataState.value = DataState.ConfirmSave(deliveryCharge)
    }

    fun setDeliveryOption(deliveryOption: EnumDeliveryOption) {
        this.deliveryOption.value = deliveryOption
    }

    fun setDeliveryCharge(deliveryCharge: DeliveryCharge) {
        this.setDeliveryOption(deliveryCharge.deliveryOption)
        this.setDeliveryProfile(deliveryCharge.deliveryProfileId)
        this.distance.value = deliveryCharge.distance.toString()
    }

    init {
        viewModelScope.launch {
            deliveryProfiles.value = repository.getAll()
        }
    }
}