package com.csi.palabakosys.app.pickup_and_deliveries

import androidx.lifecycle.viewModelScope
import com.csi.palabakosys.app.joborders.create.delivery.MenuDeliveryProfile
import com.csi.palabakosys.model.BaseFilterParams
import com.csi.palabakosys.room.repository.DeliveryProfilesRepository
import com.csi.palabakosys.viewmodels.ListViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PickupAndDeliveriesViewModel

@Inject
constructor(
    private val repository: DeliveryProfilesRepository
) : ListViewModel<MenuDeliveryProfile, BaseFilterParams>(){
    override fun filter(reset: Boolean) {
        viewModelScope.launch {
            repository.getAll().let {
                _dataState.value = DataState.LoadItems(it, reset)
            }
        }
    }
}