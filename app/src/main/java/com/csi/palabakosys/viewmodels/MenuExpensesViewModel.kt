//package com.csi.palabakosys.viewmodels
//
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.csi.palabakosys.R
//import com.csi.palabakosys.adapters.Adapter
//import com.csi.palabakosys.room.entities.EntityExpense
//import com.csi.palabakosys.room.repository.ExpensesRepository
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//
//@HiltViewModel
//class MenuExpensesViewModel
//
//@Inject
//constructor(
//    private val repository: ExpensesRepository
//) : ViewModel() {
//
////    val adapter = Adapter<EntityExpense>(R.layout.recycler_item_expense)
//    val customer = MutableLiveData<EntityExpense>()
//    val isEmpty = MutableLiveData(false);
//
//    init {
//        println("Expense View Model Created")
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