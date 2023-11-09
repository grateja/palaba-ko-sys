package com.csi.palabakosys.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.csi.palabakosys.app.dashboard.data.DateFilter
import com.csi.palabakosys.model.BaseFilterParams
import com.csi.palabakosys.model.FilterParamsInterface
import com.csi.palabakosys.util.DataState

abstract class AdvancedFilterViewModel<F: BaseFilterParams>: ViewModel() {
    private val _dataState = MutableLiveData<DataState<F?>>()
    val dataState: LiveData<DataState<F?>> = _dataState

    private val _navigationState = MutableLiveData<NavigationState>()
    val navigationState: LiveData<NavigationState> = _navigationState

    fun setInitialFilters(filters: F?) {
        advancedFilter.value = filters
    }

    fun submit() {
        advancedFilter.value?.let {
            _dataState.value = DataState.Submit(it)
        }
    }

    fun clearState() {
        _dataState.value = DataState.StateLess
    }

    fun clearNavigation() {
        _navigationState.value = NavigationState.Nothing
    }

    fun showDateFilter() {
        val dateFilter = advancedFilter.value?.dateFilter
        _navigationState.value = NavigationState.ShowDateRangePicker(dateFilter)
    }

    fun setDateRange(dateFilter: DateFilter?) {
        advancedFilter.value = advancedFilter.value.apply {
            this?.dateFilter = dateFilter
        }
    }

    val advancedFilter = MutableLiveData<F?>()

    sealed class NavigationState {
        object Nothing: NavigationState()
        data class ShowDateRangePicker(val dateFilter: DateFilter?): NavigationState()
    }
}