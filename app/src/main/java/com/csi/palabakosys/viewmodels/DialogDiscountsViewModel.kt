//package com.csi.palabakosys.viewmodels
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.csi.palabakosys.R
//import com.csi.palabakosys.adapters.Adapter
////import com.csi.palabakosys.adapters.RecyclerItem
//import com.csi.palabakosys.room.entities.EntityDiscount
//import com.csi.palabakosys.room.repository.DiscountsRepository
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//
//@HiltViewModel
//class DialogDiscountsViewModel
//@Inject
//constructor(
//    private val repository: DiscountsRepository
//) : ViewModel()
//{
////    val adapter = Adapter<EntityDiscount>(R.layout.recycler_item_select_discount)
//    fun get() {
//        viewModelScope.launch {
//            repository.getAll("").let { _list ->
////                adapter.setData(_list.map { RecyclerItem(it) })
//            }
//        }
//    }
//}