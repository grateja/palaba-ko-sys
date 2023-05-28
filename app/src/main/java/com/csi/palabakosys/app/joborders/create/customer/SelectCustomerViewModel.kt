package com.csi.palabakosys.app.joborders.create.customer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csi.palabakosys.app.customers.CustomerMinimal
import com.csi.palabakosys.room.repository.CustomerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class SelectCustomerViewModel

@Inject
constructor(
    private val repository: CustomerRepository
) : ViewModel() {
    val customers = MutableLiveData<List<CustomerMinimal>>()

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private var keyword: String? = ""

    fun setKeyword(keyword: String?) {
        this.keyword = keyword
        this.filter()
    }

    private var job: Job? = null
    fun filter() {
        job?.let {
            it.cancel()
            _loading.value = false
        }

        job = viewModelScope.launch {
            _loading.value = true
            delay(500)

            repository.getCustomersMinimal(keyword).let {
                customers.value = it
                _loading.value = false
            }
        }
    }
}