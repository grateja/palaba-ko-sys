package com.csi.palabakosys.app.joborders.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.csi.palabakosys.model.EnumItemsPerPage
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

//    private val _dataState = MutableLiveData<DataState>()
//    val dataState: LiveData<DataState> = _dataState

    val itemsPerPage = MutableLiveData(EnumItemsPerPage.ITEM_10)
    val total = MutableLiveData(0)

//    fun navigate(page: Int) {
//        val currentPage = this.page.value ?: 1
//
//        if(currentPage == 1 && page == -1) {
//            return
//        }
//
//        this.page.value = currentPage.plus(page)
//        filter(true)
//    }

//    fun showAdvancedOptions() {
//        _dataState.value = DataState.ShowAdvancedSearch(
//            keyword.value,
//            orderBy.value,
//            sortDirection.value,
//            100,//itemsPerPage.value?.value,
//            page.value
//        )
//    }

    override fun filter(reset: Boolean) {
        println("filtering")
        job?.cancel()

        job = viewModelScope.launch {
            if(!keyword.value.isNullOrBlank()) {
                delay(500)
            }

            if(reset) {
                page.value = 1
            }

            val keyword = keyword.value
            val orderBy = orderBy.value
            val sortDirection = sortDirection.value
            val page = page.value ?: 1
            val result = jobOrderRepository.load(keyword, orderBy, sortDirection, page)
//                .let {
//                items.value = it.result
//                total.value = it.count
//            }
            total.value = result.count
            _dataState.value = DataState.LoadItems(result.items, reset)
        }
    }

    fun loadMore() {
        page.value = page.value?.plus(1)
        filter(false)
    }

//    sealed class DataState {
//        data class ShowAdvancedSearch(
//            val keyword: String?,
//            val orderBy: String?,
//            val sortDirection: EnumSortDirection?,
//            val itemPerPage: Int?,
//            val page: Int?
//        ): DataState()
//    }
}