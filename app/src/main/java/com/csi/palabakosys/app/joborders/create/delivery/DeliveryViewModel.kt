package com.csi.palabakosys.app.joborders.create.delivery

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csi.palabakosys.model.DeliveryOption
import com.csi.palabakosys.room.repository.DeliveryProfilesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
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
    val deliveryOption = MutableLiveData(DeliveryOption.PICKUP_AND_DELIVERY)
    val distance = MutableLiveData(1f)
    val profile = MutableLiveData<MenuDeliveryProfile>()

    val label = MediatorLiveData<String>().apply {
        fun update() {
            value = profile.value?.vehicle.toString()
        }
        addSource(profile) {update()}
    }

    fun setDeliveryProfile(profileId: UUID) {
        this.profile.value = deliveryProfiles.value?.find { it.deliveryProfileRefId == profileId }
    }

    fun prepareDeliveryCharge() : DeliveryCharge? {
//        return DeliveryCharge(profile.value!!, distance.value!!, deliveryOption.value!!)
        val option = this.deliveryOption.value
        val distance = this.distance.value
        val profile = this.profile.value
        if(profile == null || distance == null || option == null) {
            return null
        }
        val price = option.charge * ((profile.pricePerKm * distance) + profile.baseFare)
        return DeliveryCharge(profile.deliveryProfileRefId, profile.vehicle, distance, option, price)
    }

    fun setDeliveryOption(deliveryOption: DeliveryOption) {
        this.deliveryOption.value = deliveryOption
    }

    fun setDeliveryCharge(deliveryCharge: DeliveryCharge) {
        this.setDeliveryOption(deliveryCharge.deliveryOption)
        this.setDeliveryProfile(deliveryCharge.deliveryProfileId)
        this.distance.value = deliveryCharge.distance
    }

    init {
        viewModelScope.launch {
            deliveryProfiles.value = repository.getAll()
        }
    }
}