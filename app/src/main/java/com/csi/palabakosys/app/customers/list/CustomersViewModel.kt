package com.csi.palabakosys.app.customers.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.csi.palabakosys.app.dashboard.data.DateFilter
import com.csi.palabakosys.room.repository.CustomerRepository
import com.csi.palabakosys.viewmodels.ListViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CustomersViewModel

@Inject
constructor(
    private val repository: CustomerRepository
) : ListViewModel<CustomerListItem>() {
    val total = MutableLiveData(0)
    val hideAllWithoutJo = MutableLiveData(false)

    private val _dateFilter = MutableLiveData<DateFilter?>()
    val dateFilter: LiveData<DateFilter?> = _dateFilter

    fun setDateRange(dateFilter: DateFilter?) {
        _dateFilter.value = dateFilter
    }

    override fun filter(reset: Boolean) {
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
            val page = page.value ?: 1
            val orderBy = orderBy.value
            val sortDirection = sortDirection.value
            val hideAllWithoutJo = hideAllWithoutJo.value ?: true
            val dateFilter = _dateFilter.value

            loading.value = true

            val items = repository.getListItems(
                keyword,
                orderBy,
                sortDirection,
                page,
                hideAllWithoutJo,
                dateFilter
            )
            _dataState.value = DataState.LoadItems(items.result, reset)
            total.value = items.count
            loading.value = false
        }
    }

    fun loadMore() {
        page.value = page.value?.plus(1)
        filter(false)
    }
}