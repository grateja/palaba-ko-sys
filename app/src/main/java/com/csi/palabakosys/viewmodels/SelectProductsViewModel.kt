//package com.csi.palabakosys.viewmodels
//
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
//class SelectProductsViewModel
//@Inject
//constructor(
//    private val repository: ProductRepository
//) : ViewModel() {
////    val productAdapter = Adapter<EntityProduct>(R.layout.recycler_item_product)
//
////    var onProductItemSelected: ((EntityJobOrderProduct) -> Unit) ? = null
//
////    init {
////        productAdapter.onItemClick = {
////            onProductItemSelected?.invoke(EntityJobOrderProduct(null, it.productType, it.id).apply {
////                productName = it.name
////                price = it.price
////            })
////        }
////    }
//
//    fun getAll() {
//        viewModelScope.launch {
//            repository.getAll().let {
////                productAdapter.setData(it.map { _item -> RecyclerItem(_item) })
//            }
//        }
//    }
//}