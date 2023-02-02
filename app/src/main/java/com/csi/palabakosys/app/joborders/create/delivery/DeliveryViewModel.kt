package com.csi.palabakosys.app.joborders.create.delivery

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csi.palabakosys.R
import com.csi.palabakosys.model.DeliveryOption
import com.csi.palabakosys.model.DeliveryVehicle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeliveryViewModel

@Inject
constructor() : ViewModel()
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

    fun setDeliveryProfile(profile: MenuDeliveryProfile) {
        this.profile.value = profile
    }

    fun prepareDeliveryCharge() : DeliveryCharge {
        return DeliveryCharge(profile.value!!, distance.value!!, deliveryOption.value!!)
    }

    fun setDeliveryOption(deliveryOption: DeliveryOption) {
        this.deliveryOption.value = deliveryOption
    }

    fun setDeliveryCharge(deliveryCharge: DeliveryCharge) {
        this.setDeliveryOption(deliveryCharge.deliveryOption)
        this.setDeliveryProfile(deliveryCharge.deliveryProfile)
        this.distance.value = deliveryCharge.distance
    }

    init {
        viewModelScope.launch {
            deliveryProfiles.value = listOf(
                MenuDeliveryProfile(DeliveryVehicle.TRIKE_PEDAL, 20f, 10f),
                MenuDeliveryProfile(DeliveryVehicle.TRIKE_ELECTRIC, 25f, 10f),
                MenuDeliveryProfile(DeliveryVehicle.MOTORCYCLE, 30f, 12f),
                MenuDeliveryProfile(DeliveryVehicle.TRICYCLE, 40f, 12f),
                MenuDeliveryProfile(DeliveryVehicle.SEDAN, 50f, 15f),
                MenuDeliveryProfile(DeliveryVehicle.MPV, 60f, 15f),
                MenuDeliveryProfile(DeliveryVehicle.SMALL_VAN, 70f, 17f),
            )
        }
    }
}