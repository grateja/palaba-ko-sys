package com.csi.palabakosys.app.machines.usage

import androidx.lifecycle.*
import com.csi.palabakosys.app.dashboard.data.DateFilter
import com.csi.palabakosys.room.entities.EntityMachineUsageDetails
import com.csi.palabakosys.room.repository.MachineRepository
import com.csi.palabakosys.viewmodels.ListViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MachineUsageViewModel

@Inject
constructor(
    private val machineRepository: MachineRepository
): ListViewModel<EntityMachineUsageDetails>() {
    private val _machineId = MutableLiveData<UUID>()
    private val _dateFilter = MutableLiveData<DateFilter>()

    fun setMachineId(machineId: UUID) {
        _machineId.value = machineId
    }

    fun setDateFilter(dateFilter: DateFilter) {
        _dateFilter.value = dateFilter
    }

    fun loadMore() {
        page.value = page.value?.plus(1)
        filter(false)
    }

    override fun filter(reset: Boolean) {
        viewModelScope.launch {
            if(reset) {
                page.value = 1
            }
            val machineId = _machineId.value ?: return@launch
            val dateFilter = _dateFilter.value
            val page = page.value ?: 1
            val items = machineRepository.getMachineUsage(machineId, page, dateFilter)
            _dataState.value = DataState.LoadItems(items, reset)
        }
    }
}