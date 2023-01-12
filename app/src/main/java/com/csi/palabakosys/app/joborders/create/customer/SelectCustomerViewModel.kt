package com.csi.palabakosys.app.joborders.create.customer

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csi.palabakosys.app.customers.CustomerMinimal
import com.csi.palabakosys.room.repository.CustomerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectCustomerViewModel

@Inject
constructor(
    private val repository: CustomerRepository
) : ViewModel() {
    val customers = MutableLiveData<List<CustomerMinimal>>()
    fun searchMinimal(keyword: String?) {
        viewModelScope.launch {
            repository.getCustomersMinimal(keyword).let {
                customers.value = it
            }
        }
    }
}