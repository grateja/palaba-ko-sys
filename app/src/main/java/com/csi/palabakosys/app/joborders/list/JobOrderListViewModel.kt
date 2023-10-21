package com.csi.palabakosys.app.joborders.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.csi.palabakosys.app.dashboard.data.DateFilter
import com.csi.palabakosys.model.EnumJoFilterBy
import com.csi.palabakosys.model.EnumPaymentStatus
import com.csi.palabakosys.room.repository.JobOrderRepository
import com.csi.palabakosys.viewmodels.ListViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.*
import javax.inject.Inject

@HiltViewModel
class JobOrderListViewModel

@Inject
constructor(
    private val jobOrderRepository: JobOrderRepository
) : ListViewModel<JobOrderListItem>() {

    val paymentStatus = MutableLiveData(EnumPaymentStatus.ALL)
    val total = MutableLiveData(0)
    val hideDeleted = MutableLiveData(true)
    val customerId = MutableLiveData<UUID?>()
    private val _navigationState = MutableLiveData<NavigationState>()
    val navigationState: LiveData<NavigationState> = _navigationState

    private val _dateFilter = MutableLiveData<DateFilter?>()
    val dateFilter: LiveData<DateFilter?> = _dateFilter
    val filterBy = MutableLiveData<EnumJoFilterBy>()

    fun setFilterBy(filterBy: EnumJoFilterBy) {
        this.filterBy.value = filterBy
    }

    override fun filter(reset: Boolean) {
        println("filtering $reset")
        job?.cancel()

        job = viewModelScope.launch {
            if(!keyword.value.isNullOrBlank()) {
                delay(500)
            } else {
                delay(100)
            }

            if(reset) {
                page.value = 1
            }

            val keyword = keyword.value
            val orderBy = orderBy.value
            val sortDirection = sortDirection.value
            val page = page.value ?: 1
            val paymentStatus = paymentStatus.value
            val customerId = customerId.value
            val filterBy = filterBy.value

            val dateFilter = _dateFilter.value

            val result = jobOrderRepository.load(keyword, orderBy, sortDirection, page, paymentStatus, customerId, filterBy, dateFilter)
            println("filter by")
            println(filterBy)

            total.value = result.count

            _dataState.value = DataState.LoadItems(result.items, reset)
        }
    }

    fun loadMore() {
        page.value = page.value?.plus(1)
        filter(false)
    }

    fun setCustomerId(customerId: UUID?) {
        this.customerId.value = customerId
    }

    fun setDateRange(dateFilter: DateFilter) {
        _dateFilter.value = dateFilter
        filter(true)
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

    fun clearDates() {
        _dateFilter.value = null
    }

    sealed class NavigationState {
        object StateLess: NavigationState()
        data class OpenDateFilter(val dateFilter: DateFilter): NavigationState()
    }
}