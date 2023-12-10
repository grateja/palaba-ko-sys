package com.csi.palabakosys.app.joborders.unpaid.prompt

import androidx.lifecycle.*
import com.csi.palabakosys.app.customers.CustomerMinimal
import com.csi.palabakosys.room.repository.JobOrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class JobOrdersUnpaidPromptViewModel

@Inject
constructor(
    private val repository: JobOrderRepository
) : ViewModel() {
    private val _dataState = MutableLiveData<DataState>()
    val dataState: LiveData<DataState> = _dataState

    private val _customer = MutableLiveData<CustomerMinimal>()
    val customer: LiveData<CustomerMinimal> = _customer

    // private val _jobOrders = MutableLiveData<List<JobOrderPaymentMinimal>>()
    val jobOrders = _customer.switchMap { repository.getAllUnpaidByCustomerIdAsLiveData(it.id) } //: LiveData<List<JobOrderPaymentMinimal>> = _jobOrders

    val amountToPay = MediatorLiveData<Float>().apply {
        fun update() {
            value = jobOrders.value?.sumOf { it.discountedAmount.toDouble() }?.toFloat()
        }
        addSource(jobOrders) { update() }
    }

//    fun load(customerId: UUID) {
//        viewModelScope.launch {
//            _jobOrders.value = repository.getAllUnpaidByCustomerId(customerId)
//        }
//    }

    fun openPayment() {
        customer.value?.let {
            _dataState.value = DataState.OpenPayment(it.id)
        }
    }

    fun openJobOrder() {
        customer.value?.let {
            _dataState.value = DataState.OpenJobOrder(it)
        }
    }

    fun setCustomer(customer: CustomerMinimal) {
        _customer.value = customer
    }

    fun resetState() {
        _dataState.value = DataState.StateLess
    }

    sealed class DataState {
        object StateLess : DataState()
        class OpenPayment(val customerId: UUID) : DataState()
        class OpenJobOrder(val customer: CustomerMinimal) : DataState()
    }
}