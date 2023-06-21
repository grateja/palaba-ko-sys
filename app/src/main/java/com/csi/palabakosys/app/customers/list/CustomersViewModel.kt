package com.csi.palabakosys.app.customers.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.csi.palabakosys.room.repository.CustomerRepository
import com.csi.palabakosys.viewmodels.ListViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CustomersViewModel

@Inject
constructor(
    private val repository: CustomerRepository
) : ListViewModel<CustomerListItem>() {
    val total = MutableLiveData(0)

    override fun filter(reset: Boolean) {
        viewModelScope.launch {
            if(reset) {
                page.value = 1
            }
            val keyword = keyword.value
            val page = page.value ?: 1

            loading.value = true

            val items = repository.getListItems(
                keyword,
                page
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