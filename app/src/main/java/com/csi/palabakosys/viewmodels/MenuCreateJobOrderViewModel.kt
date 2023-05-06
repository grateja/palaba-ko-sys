//package com.csi.palabakosys.viewmodels
//
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
////import com.csi.palabakosys.adapters.SelectedJobOrderProductsAdapter
//import com.csi.palabakosys.room.repository.CustomerRepository
//import com.csi.palabakosys.room.repository.JobOrderRepository
////import com.csi.palabakosys.adapters.SelectedJobOrderServicesAdapter
//import com.csi.palabakosys.room.entities.*
//import com.csi.palabakosys.util.CRUDActionEnum
//import com.csi.palabakosys.util.DataState
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//
//@HiltViewModel
//class MenuCreateJobOrderViewModel
//@Inject
//constructor(
//    private val repository: JobOrderRepository,
//    private val customerRepository: CustomerRepository
//) : ViewModel() {
//    val currentJobOrder = MutableLiveData<EntityJobOrder>()
////    val servicesAdapter = SelectedJobOrderServicesAdapter() //Adapter<JobOrderService>(R.layout.recycler_item_job_order_service)
////    val productsAdapter = SelectedJobOrderProductsAdapter() //Adapter<JobOrderProduct>(R.layout.recycler_item_job_order_product)
////    val paymentAdapter = Adapter<EntityJobOrderPayment>(R.layout.recycler_item_payment)
//    val customer = MutableLiveData<EntityCustomer?>()
//    val dataState = MutableLiveData<DataState<EntityJobOrderWithItems>>()
//    val totalAmount = MutableLiveData(0f)
//    val crudAction = MutableLiveData(CRUDActionEnum.CREATE)
//
//    fun getId() : String? {
//        return currentJobOrder.value?.id?.toString()
//    }
//
//    init {
////        servicesAdapter.onDataSetChanged = {
////            computeTotalAmount()
////        }
////        productsAdapter.onDataSetChanged = {
////            computeTotalAmount()
////        }
//    }
//
//    fun get(id: String?) {
//        currentJobOrder.value?.let {
//            if(it.id.toString() == id) return
//        }
//        viewModelScope.launch {
//            val jo = repository.get(id)
//            val nextJO = repository.getNextJONumber()
//            jo.let {
//                if(it != null) {
//                    currentJobOrder.value = it.jobOrder
//                    customer.value = it.customer
////                    servicesAdapter.setData(it.services.map { _item -> RecyclerItem(_item) })
////                    productsAdapter.setData(it.products.map { _item -> RecyclerItem(_item) })
////                    paymentAdapter.setData(it.payments)
//                    crudAction.value = CRUDActionEnum.UPDATE
//                } else {
////                    currentJobOrder.value = EntityJobOrder(nextJO)
//                    crudAction.value = CRUDActionEnum.CREATE
//                }
//            }
//        }
//    }
//
//    fun addWashService(service: EntityService) {
////        servicesAdapter.add(RecyclerItem(EntityJobOrderService(UUID.fromString(getId()), service.machineType, service.id).apply {
////            serviceName = service.name
////            price = service.price
////            cashBack = service.cashBack
////        }))
//    }
////    fun addDryService(service: EntityServiceDry) {
////        servicesAdapter.add(RecyclerItem(EntityJobOrderService(UUID.fromString(getId()), service.machineType, service.id).apply {
////            serviceName = service.name
////            price = service.price
////            cashBack = service.cashBack
////        }))
////    }
//    fun addOtherService(service: EntityServiceOther) {
////        servicesAdapter.add(RecyclerItem(EntityJobOrderService(UUID.fromString(getId()), null, service.id).apply {
////            serviceName = service.name
////            price = service.price
////        }))
//    }
//    fun addProduct(product: EntityProduct) {
////        productsAdapter.add(RecyclerItem(EntityJobOrderProduct(UUID.fromString(getId()), product.productType, product.id).apply {
////            productName = product.name
////            price = product.price
////        }))
//    }
//    private fun computeTotalAmount() {
////        val servicesAmount = servicesAdapter.getItems().filter { it.getItem().deletedAt == null }.map { it.getItem().price * it.getItem().quantity }.sum()
////        val productsAmount = productsAdapter.getItems().filter { it.getItem().deletedAt == null }.map { it.getItem().price * it.getItem().quantity }.sum()
////        totalAmount.value = (servicesAmount + productsAmount)
//    }
//
//    fun setCustomerById(id: String) {
//        viewModelScope.launch {
//            customerRepository.get(id)?.let {
//                customer.value = it
//            }
//        }
//    }
//    fun removeCustomer() {
//        customer.value = null
//    }
//
//    fun save() {
////        viewModelScope.launch {
////            currentJobOrder.value?.let { _jobOrder ->
////                customer.value?.let { _customer ->
////                    _jobOrder.customerName = _customer.name
////                    _jobOrder.customerId = _customer.id
////                    repository.save(
////                        EntityJobOrderWithItems(
////                            _jobOrder,
////                            _customer,
////                            servicesAdapter.getItems().map { it.getItem() },
////                            productsAdapter.getItems().map { it.getItem() }
////                        )
////                    )?.let {
////                        dataState.value = DataState.Success(it)
////                    }
////                    return@launch
////                }
////                dataState.value = DataState.Invalidate("No customer selected")
////            }
////        }
//    }
//}