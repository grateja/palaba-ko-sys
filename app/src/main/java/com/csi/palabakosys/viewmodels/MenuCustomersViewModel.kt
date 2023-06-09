//package com.csi.palabakosys.viewmodels
//
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.csi.palabakosys.R
//import com.csi.palabakosys.room.repository.CustomerRepository
//import com.csi.palabakosys.adapters.Adapter
//import com.csi.palabakosys.room.entities.EntityCustomer
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//
//@HiltViewModel
//class MenuCustomersViewModel
//
//@Inject
//constructor(
//    private val repository: CustomerRepository
//) : ViewModel() {
//
////    val adapter = Adapter<EntityCustomer>(R.layout.recycler_item_customer_full)
//    val customer = MutableLiveData<EntityCustomer>()
//    val isEmpty = MutableLiveData(false);
//
//    init {
//        println("Customer View Model Created")
////        adapter.onItemClick = {
////            customer.value = it.getItem()
////        }
//    }
//
//    fun getAll(keyword: String?) {
//        viewModelScope.launch {
////            repository.getAll(keyword ?: "").let {
////                adapter.setData(it.map { _item -> RecyclerItem(_item) })
////                isEmpty.value = it.isEmpty()
////            }
//        }
//    }
//
//}