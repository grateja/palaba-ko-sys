package com.csi.palabakosys.app.joborders.create.services

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csi.palabakosys.app.joborders.create.ui.QuantityModel
import com.csi.palabakosys.model.MachineType
import com.csi.palabakosys.model.WashType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AvailableServicesViewModel

@Inject
constructor() : ViewModel() {
    val availableServices = MutableLiveData<List<MenuServiceItem>>()
    val dataState = MutableLiveData<DataState>()

    init {
        loadServices()
    }

    private fun loadServices() {
        viewModelScope.launch {
            val services = listOf(
                MenuServiceItem("rw-hw", "Hot Wash", 46, 90f, MachineType.REGULAR_WASHER, WashType.HOT),
                MenuServiceItem("rw-ww", "Warm Wash", 36, 70f, MachineType.REGULAR_WASHER, WashType.WARM),
                MenuServiceItem("rw-dw", "Delicate Wash", 21, 40f, MachineType.REGULAR_WASHER, WashType.DELICATE),
                MenuServiceItem("rw-sw", "Super Wash", 47, 100f, MachineType.REGULAR_WASHER, WashType.SUPER_WASH),
                MenuServiceItem("rd-rd", "Regular Dry", 40, 70f, MachineType.REGULAR_DRYER, null),
                MenuServiceItem("rd-ad", "Additional Dry", 10, 20f, MachineType.REGULAR_DRYER, null),
                MenuServiceItem("tw-hw", "Hot Wash", 46, 100f, MachineType.TITAN_WASHER, WashType.HOT),
                MenuServiceItem("tw-ww", "Warm Wash", 36, 90f, MachineType.TITAN_WASHER, WashType.WARM),
                MenuServiceItem("tw-dw", "Delicate Wash", 21, 60f, MachineType.TITAN_WASHER, WashType.DELICATE),
                MenuServiceItem("tw-sw", "Super Wash", 47, 120f, MachineType.TITAN_WASHER, WashType.SUPER_WASH),
                MenuServiceItem("td-rd", "Regular Dry", 40, 90f, MachineType.TITAN_DRYER, null),
                MenuServiceItem("td-ad", "Additional Dry", 10, 25f, MachineType.TITAN_DRYER, null),
            )
            availableServices.value = services
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
        val service = availableServices.value?.find { it.id == quantityModel.id }?.apply {
            selected = true
            quantity = quantityModel.quantity
        }
        dataState.value = DataState.UpdateService(service!!)
    }

    fun removeService(service: QuantityModel) {
        val item = availableServices.value?.find { it.id == service.id }?.apply {
            this.selected = false
            this.quantity = 0
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