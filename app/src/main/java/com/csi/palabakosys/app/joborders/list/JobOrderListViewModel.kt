package com.csi.palabakosys.app.joborders.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.csi.palabakosys.room.repository.JobOrderRepository
import com.csi.palabakosys.viewmodels.ListViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JobOrderListViewModel

@Inject
constructor(
    private val jobOrderRepository: JobOrderRepository
) : ListViewModel<JobOrderListItem>() {

    val total = MutableLiveData(0)
    val hideDeleted = MutableLiveData(true)

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

            val result = jobOrderRepository.load(keyword, orderBy, sortDirection, page)

            total.value = result.count

            _dataState.value = DataState.LoadItems(result.items, reset)
        }
    }

    fun loadMore() {
        page.value = page.value?.plus(1)
        filter(false)
    }
}