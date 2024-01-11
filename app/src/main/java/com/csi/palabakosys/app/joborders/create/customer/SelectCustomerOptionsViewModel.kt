//package com.csi.palabakosys.app.joborders.create.customer
//
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.switchMap
//import com.csi.palabakosys.app.joborders.create.customer.SelectCustomerOptionsFragment.Companion.ACTION_CREATE_NEW
//import com.csi.palabakosys.room.repository.CustomerRepository
//import com.csi.palabakosys.util.DataState
//import dagger.hilt.android.lifecycle.HiltViewModel
//import java.util.UUID
//import javax.inject.Inject
//
//@HiltViewModel
//class SelectCustomerOptionsViewModel
//
//@Inject
//constructor(
//    private val customerRepository: CustomerRepository
//) : ViewModel(){
//    private val _dataState = MutableLiveData<DataState<Pair<String, UUID?>>>()
//    val dataState: LiveData<DataState<Pair<String, UUID?>>> = _dataState
//
//    private val _customerId = MutableLiveData<UUID?>()
//    val customerId: LiveData<UUID?> = _customerId
//
//    val customer = _customerId.switchMap { customerRepository.getCustomerAsLiveData(it) }
//
//    fun setCustomerId(customerId: UUID?) {
//        _customerId.value = customerId
//    }
//
//    fun resetState() {
//        _dataState.value = DataState.StateLess
//    }
//
//    fun submit(action: String) {
//        val customerId = if(action == ACTION_CREATE_NEW) null else _customerId.value
//        _dataState.value = DataState.Submit(Pair(action, customerId))
//    }
//}