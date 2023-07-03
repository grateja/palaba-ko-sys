package com.csi.palabakosys.app.customers.preview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csi.palabakosys.room.entities.EntityCustomer
import com.csi.palabakosys.room.repository.CustomerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CustomerPreviewViewModel

@Inject
constructor(
    private val repository: CustomerRepository
) : ViewModel() {
    private val _customer = MutableLiveData<EntityCustomer>()
    val customer: LiveData<EntityCustomer> = _customer

    fun load(customerId: UUID) {
        viewModelScope.launch {
            _customer.value = repository.get(customerId)
        }
    }
}