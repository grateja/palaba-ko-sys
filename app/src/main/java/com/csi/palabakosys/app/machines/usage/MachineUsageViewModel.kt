package com.csi.palabakosys.app.machines.usage

import androidx.lifecycle.*
import com.csi.palabakosys.app.dashboard.data.DateFilter
import com.csi.palabakosys.app.expenses.ExpensesViewModel
import com.csi.palabakosys.room.entities.EntityMachineUsageDetails
import com.csi.palabakosys.room.repository.MachineRepository
import com.csi.palabakosys.viewmodels.ListViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MachineUsageViewModel

@Inject
constructor(
    private val machineRepository: MachineRepository
): ListViewModel<EntityMachineUsageDetails>() {
    private val _machineId = MutableLiveData<UUID>()

    private val _dateFilter = MutableLiveData<DateFilter?>()
    val dateFilter: MutableLiveData<DateFilter?> = _dateFilter

    private val _navigationState = MutableLiveData<NavigationState>()
    val navigationState: LiveData<NavigationState> = _navigationState

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
        job?.let {
            it.cancel()
            loading.value = false
        }
        job = viewModelScope.launch {
            delay(500)
            if(reset) {
                page.value = 1
            }
            val machineId = _machineId.value ?: return@launch
            val dateFilter = _dateFilter.value
            val page = page.value ?: 1
            val keyword = keyword.value
            val items = machineRepository.getMachineUsage(machineId, keyword, page, dateFilter)
            _dataState.value = DataState.LoadItems(items, reset)
        }
    }
    fun clearDates() {
        _dateFilter.value = null
    }

    fun showDatePicker() {
        _dateFilter.value.let {
            val dateFilter = it ?: DateFilter(LocalDate.now(), null)
            _navigationState.value = NavigationState.OpenDateFilter(dateFilter)
        }
    }

    override fun clearState() {
        _navigationState.value = NavigationState.StateLess
        super.clearState()
    }

    sealed class NavigationState {
        object StateLess: NavigationState()
        data class OpenDateFilter(val dateFilter: DateFilter): NavigationState()
    }
}