//package com.csi.palabakosys.viewmodels
//
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.csi.palabakosys.R
//import com.csi.palabakosys.room.repository.ProductRepository
//import com.csi.palabakosys.adapters.Adapter
//import com.csi.palabakosys.room.entities.EntityProduct
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//
//@HiltViewModel
//class ProductsViewModel
//@Inject
//constructor(
//    private val repository: ProductRepository
//) : ViewModel() {
////    val adapter = Adapter<EntityProduct>(R.layout.recycler_item_product)
//
//    val product = MutableLiveData<EntityProduct>()
//    val isEmpty = MutableLiveData(false);
//
//    init {
////        adapter.onItemClick = {
////            product.value = it.getItem()
////        }
//    }
//
//    fun getAll() {
//        viewModelScope.launch {
//            val services = repository.getAll()
////            services.forEach {
////                println(it.name)
////            }
////            adapter.setData(services.map { _item -> RecyclerItem(_item) })
//            isEmpty.value = services.isEmpty()
//        }
//    }
//}