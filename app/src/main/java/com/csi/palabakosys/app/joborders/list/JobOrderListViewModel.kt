package com.csi.palabakosys.app.joborders.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csi.palabakosys.room.entities.EntityJobOrder
import com.csi.palabakosys.room.repository.JobOrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class JobOrderListViewModel

@Inject
constructor(
    private val jobOrderRepository: JobOrderRepository
) : ViewModel() {
    private val _keyword = MutableLiveData("")
    val keyword: LiveData<String> = _keyword

    private val _orderBy = MutableLiveData("created_at")
    val orderBy: LiveData<String> = _orderBy

    private val _legendSort = MutableLiveData("DESC")
    val legendSort: LiveData<String> = _legendSort

    private val _jobOrders = MutableLiveData<List<JobOrderListItem>>()
    val jobOrders: LiveData<List<JobOrderListItem>> = _jobOrders

    fun setKeyword(keyword: String?) {
        _keyword.value = keyword
        filter()
    }

    private var job: Job? = null
    fun filter() {
        job?.cancel()

        job = viewModelScope.launch {
            println("Waiting 500 ms")
            delay(500)
            _jobOrders.value = jobOrderRepository.load(keyword.value, orderBy.value, legendSort.value)
        }
    }
}