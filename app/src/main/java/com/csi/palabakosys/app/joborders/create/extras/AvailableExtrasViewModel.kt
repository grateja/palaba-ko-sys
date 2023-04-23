package com.csi.palabakosys.app.joborders.create.extras

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csi.palabakosys.app.joborders.create.shared_ui.QuantityModel
import com.csi.palabakosys.room.repository.ExtrasRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AvailableExtrasViewModel

@Inject
constructor(
    private val repository: ExtrasRepository
) : ViewModel() {
    val availableExtras = MutableLiveData<List<MenuExtrasItem>>()
    val dataState = MutableLiveData<DataState>()

    init {
        loadServices()
    }

    private fun loadServices() {
        viewModelScope.launch {
            repository.getAll("").let {
                availableExtras.value = it
            }
        }
    }

    fun setPreSelectedServices(services: List<MenuExtrasItem>?) {
        services?.forEach { msi ->
            availableExtras.value?.find { msi.id == it.id }?.apply {
                this.selected = true
                this.quantity = msi.quantity
            }
        }
    }

    fun putService(quantityModel: QuantityModel) {
        val service = availableExtras.value?.find { it.id == quantityModel.id }?.apply {
            selected = true
            quantity = quantityModel.quantity
        }
        dataState.value = DataState.UpdateService(service!!)
    }

    fun removeService(service: QuantityModel) {
        val item = availableExtras.value?.find { it.id == service.id }?.apply {
            this.selected = false
            this.quantity = 1
        }
        dataState.value = DataState.UpdateService(item!!)
    }

    fun prepareSubmit() {
        val list = availableExtras.value?.filter { it.selected }
        list?.let {
            dataState.value = DataState.Submit(it)
        }
    }

    fun resetState() {
        dataState.value = DataState.StateLess
    }

    sealed class DataState {
        object StateLess: DataState()
        data class UpdateService(val extrasItem: MenuExtrasItem) : DataState()
        data class Submit(val selectedItems: List<MenuExtrasItem>) : DataState()
    }
}