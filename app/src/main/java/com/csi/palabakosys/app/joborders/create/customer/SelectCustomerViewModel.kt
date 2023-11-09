package com.csi.palabakosys.app.joborders.create.customer

import androidx.lifecycle.viewModelScope
import com.csi.palabakosys.app.customers.CustomerMinimal
import com.csi.palabakosys.model.BaseFilterParams
import com.csi.palabakosys.room.repository.CustomerRepository
import com.csi.palabakosys.viewmodels.ListViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class SelectCustomerViewModel

@Inject
constructor(
    private val repository: CustomerRepository
) : ListViewModel<CustomerMinimal, BaseFilterParams>() {
//    val items = MutableLiveData<List<CustomerMinimal>>()

//    private val _loading = MutableLiveData(false)
//    val loading: LiveData<Boolean> = _loading

//    private var keyword: String? = ""

//    fun setKeyword(keyword: String?) {
//        this.keyword = keyword
//        this.filter()
//    }

//    private var job: Job? = null
    override fun filter(reset: Boolean) {
        job?.let {
            it.cancel()
            loading.value = false
        }

        job = viewModelScope.launch {
            loading.value = true
            delay(500)

            if(reset) {
                page.value = 1
            }

            val filterParams = filterParams.value

            val keyword = keyword.value
            val orderBy = filterParams?.orderBy
            val sortDirection = filterParams?.sortDirection
            val page = page.value ?: 1

            val result = repository.getCustomersMinimal(
                keyword,
                page
            )
            _dataState.value = DataState.LoadItems(
                result,
                reset
            )
            loading.value = false
        }
    }

    fun loadMore() {
        page.value = page.value?.plus(1)
        filter(false)
    }
}