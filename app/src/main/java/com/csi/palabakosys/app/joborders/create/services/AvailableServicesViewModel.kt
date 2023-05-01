package com.csi.palabakosys.app.joborders.create.services

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csi.palabakosys.app.joborders.create.shared_ui.QuantityModel
import com.csi.palabakosys.room.repository.WashServiceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
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
            availableServices.value = serviceRepository.getAll()
        }
    }

    fun setPreSelectedServices(services: List<MenuServiceItem>?) {
        println("available services")
        println(availableServices.value)
        services?.forEach { msi ->
            println("msi id")
            println(msi.serviceRefId)
            availableServices.value?.find {
                println("sid")
                println(it.serviceRefId)
                msi.serviceRefId.toString() == it.serviceRefId.toString()
            }?.apply {
                this.joServiceItemId = msi.joServiceItemId
                this.selected = true
                this.quantity = msi.quantity
                this.used = msi.used
            }
        }
    }

    fun putService(quantityModel: QuantityModel) {
        val service = availableServices.value?.find { it.serviceRefId == quantityModel.id }?.apply {
            println("used")
            println(used)

            println("quantity")
            println(quantityModel.quantity)
            if(quantityModel.quantity < used) {
                dataState.value = DataState.InvalidOperation("Cannot remove used service")
                return
            }
            selected = true
            quantity = quantityModel.quantity
        }
        dataState.value = DataState.UpdateService(service!!)
    }

    fun removeService(service: QuantityModel) {
        val item = availableServices.value?.find { it.serviceRefId == service.id }?.apply {
            if(this.joServiceItemId != null) {
                dataState.value = DataState.InvalidOperation("Cannot remove saved service")
                return
            }
            this.selected = false
            this.quantity = 1
        }
        dataState.value = DataState.UpdateService(item!!)
    }

    fun prepareSubmit() {
        val list = availableServices.value?.filter { it.selected }
        list?.let { it ->
            dataState.value = DataState.Submit(it)
        }
    }

    fun resetState() {
        dataState.value = DataState.StateLess
    }

    sealed class DataState {
        object StateLess: DataState()
        data class UpdateService(val serviceItem: MenuServiceItem) : DataState()
        data class InvalidOperation(val message: String) : DataState()
        data class Submit(val selectedItems: List<MenuServiceItem>) : DataState()
    }
}