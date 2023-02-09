package com.csi.palabakosys.app.joborders.create.services

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csi.palabakosys.app.joborders.create.ui.QuantityModel
import com.csi.palabakosys.model.MachineType
import com.csi.palabakosys.model.WashType
import com.csi.palabakosys.room.entities.EntityServiceWash
import com.csi.palabakosys.room.repository.WashServiceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AvailableServicesViewModel

@Inject
constructor(
    private val serviceRepository: WashServiceRepository
) : ViewModel() {
    val availableServices = MutableLiveData<List<MenuServiceItem>>()
    val dataState = MutableLiveData<DataState>()

    init {
        loadServices()
    }

    private fun loadServices() {
        viewModelScope.launch {
//            val services = listOf(
//                MenuServiceItem(UUID.fromString("b5d4ce14-a701-11ed-afa1-0242ac120002") , "Hot Wash", 46, 90f, MachineType.REGULAR_WASHER, WashType.HOT),
//                MenuServiceItem(UUID.fromString("b5d4ce14-a701-11ed-afa1-0242ac120003"), "Warm Wash", 36, 70f, MachineType.REGULAR_WASHER, WashType.WARM),
//                MenuServiceItem(UUID.fromString("b5d4ce14-a701-11ed-afa1-0242ac120004"), "Delicate Wash", 21, 40f, MachineType.REGULAR_WASHER, WashType.DELICATE),
//                MenuServiceItem(UUID.fromString("b5d4ce14-a701-11ed-afa1-0242ac120005"), "Super Wash", 47, 100f, MachineType.REGULAR_WASHER, WashType.SUPER_WASH),
//                MenuServiceItem(UUID.fromString("b5d4ce14-a701-11ed-afa1-0242ac120006"), "Regular Dry", 40, 70f, MachineType.REGULAR_DRYER, null),
//                MenuServiceItem(UUID.fromString("b5d4ce14-a701-11ed-afa1-0242ac120007"), "Additional Dry", 10, 20f, MachineType.REGULAR_DRYER, null),
//                MenuServiceItem(UUID.fromString("b5d4ce14-a701-11ed-afa1-0242ac120008"), "Hot Wash", 46, 100f, MachineType.TITAN_WASHER, WashType.HOT),
//                MenuServiceItem(UUID.fromString("b5d4ce14-a701-11ed-afa1-0242ac120009"), "Warm Wash", 36, 90f, MachineType.TITAN_WASHER, WashType.WARM),
//                MenuServiceItem(UUID.fromString("b5d4ce14-a701-11ed-afa1-0242ac120010"), "Delicate Wash", 21, 60f, MachineType.TITAN_WASHER, WashType.DELICATE),
//                MenuServiceItem(UUID.fromString("b5d4ce14-a701-11ed-afa1-0242ac120011"), "Super Wash", 47, 120f, MachineType.TITAN_WASHER, WashType.SUPER_WASH),
//                MenuServiceItem(UUID.fromString("b5d4ce14-a701-11ed-afa1-0242ac120012"), "Regular Dry", 40, 90f, MachineType.TITAN_DRYER, null),
//                MenuServiceItem(UUID.fromString("b5d4ce14-a701-11ed-afa1-0242ac120013"), "Additional Dry", 10, 25f, MachineType.TITAN_DRYER, null),
//            )
//            serviceRepository.save(
//                EntityServiceWash().apply {
//                    name = "Hot Wash"
//                    minutes = 46
//                    price = 90f
//                    machineType = MachineType.REGULAR_WASHER
//                    washType = WashType.HOT
//                }
//            )
            availableServices.value = serviceRepository.getAll()
        }
    }

    fun setPreSelectedServices(services: List<MenuServiceItem>?) {
        services?.forEach { msi ->
            availableServices.value?.find { msi.id == it.id }?.apply {
                this.selected = true
                this.quantity = msi.quantity
            }
        }
    }

    fun putService(quantityModel: QuantityModel) {
        val service = availableServices.value?.find { it.id.toString() == quantityModel.id }?.apply {
            selected = true
            quantity = quantityModel.quantity
        }
        dataState.value = DataState.UpdateService(service!!)
    }

    fun removeService(service: QuantityModel) {
        val item = availableServices.value?.find { it.id.toString() == service.id }?.apply {
            this.selected = false
            this.quantity = 1
        }
        dataState.value = DataState.UpdateService(item!!)
    }

    fun prepareSubmit() {
        val list = availableServices.value?.filter { it.selected }
        list?.let {
            dataState.value = DataState.Submit(it)
        }
    }

    fun resetState() {
        dataState.value = DataState.StateLess
    }

    sealed class DataState {
        object StateLess: DataState()
        data class UpdateService(val serviceItem: MenuServiceItem) : DataState()
        data class Submit(val selectedItems: List<MenuServiceItem>) : DataState()
    }
}