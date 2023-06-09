//package com.csi.palabakosys.viewmodels
//
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.csi.palabakosys.R
//import com.csi.palabakosys.adapters.Adapter
//import com.csi.palabakosys.room.entities.EntityDiscount
//import com.csi.palabakosys.room.repository.DiscountsRepository
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//
//@HiltViewModel
//class MenuDiscountsViewModel
//
//@Inject
//constructor(
//    private val repository: DiscountsRepository
//) : ViewModel() {
//
////    val adapter = Adapter<EntityDiscount>(R.layout.recycler_item_discount)
//    val customer = MutableLiveData<EntityDiscount>()
//    val isEmpty = MutableLiveData(false);
//
//    init {
////        adapter.onItemClick = {
////            customer.value = it.getItem()
////        }
//    }
//
//    fun getAll(keyword: String?) {
//        viewModelScope.launch {
//            repository.getAll(keyword ?: "").let {
////                adapter.setData(it.map { _item -> RecyclerItem(_item) })
//                isEmpty.value = it.isEmpty()
//            }
//        }
//    }
//
//}