//package com.csi.palabakosys.viewmodels
//
//import androidx.lifecycle.MediatorLiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.csi.palabakosys.R
//import com.csi.palabakosys.room.repository.JobOrderRepository
//import com.csi.palabakosys.adapters.Adapter
//import com.csi.palabakosys.model.Role
//import com.csi.palabakosys.preferences.AppPreferenceRepository
//import com.csi.palabakosys.room.entities.EntityJobOrderListItem
//import com.csi.palabakosys.room.entities.EntityUser
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//
//@HiltViewModel
//class JobOrdersViewModel
//@Inject
//constructor(
//    private val repository: JobOrderRepository,
//    private val appPreference: AppPreferenceRepository
//): ViewModel() {
//    val jobOrder = MutableLiveData<EntityJobOrderListItem>()
////    val adapter = Adapter<EntityJobOrderListItem>(R.layout.recycler_item_job_order_full)
//    val isEmpty = MutableLiveData<Boolean>()
////    private val activeUser: MutableLiveData<EntityUser?>
////        get() = appPreference.activeUser
//
////    val isAdmin = MediatorLiveData<Boolean>().apply {
////        fun update() {
////            activeUser.value?.let {
////                value = it.role == Role.ADMIN
////            }
////        }
////        addSource(activeUser) {update()}
////    }
//
//    init {
////        adapter.onItemClick = {
////            jobOrder.value = it.getItem()
////        }
//    }
//
//    fun getAll(keyword: String?, includeVoid: Boolean) {
//        viewModelScope.launch {
//            repository.getAllWithTotalAmount(keyword?: "", includeVoid).let { l ->
////                adapter.setData(l.map { RecyclerItem(it) })
//                isEmpty.value = l.isEmpty()
////                l.forEach{
////                    println("date paid")
////                    println(it.jobOrderNumber)
////                    println(it.datePaid)
////                }
//            }
//        }
//    }
//}