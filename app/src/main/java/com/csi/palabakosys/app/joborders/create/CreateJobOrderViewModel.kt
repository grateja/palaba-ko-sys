package com.csi.palabakosys.app.joborders.create

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csi.palabakosys.app.customers.CustomerMinimal
import com.csi.palabakosys.app.joborders.create.delivery.DeliveryCharge
import com.csi.palabakosys.app.joborders.create.discount.MenuDiscount
import com.csi.palabakosys.app.joborders.create.extras.MenuExtrasItem
import com.csi.palabakosys.app.joborders.create.products.MenuProductItem
import com.csi.palabakosys.app.joborders.create.services.MenuServiceItem
import com.csi.palabakosys.model.DiscountApplicable
import com.csi.palabakosys.room.entities.*
import com.csi.palabakosys.room.repository.JobOrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class CreateJobOrderViewModel

@Inject
constructor(
    val jobOrderRepository: JobOrderRepository
) : ViewModel() {
    sealed class DataState {
        object StateLess: DataState()
        object SaveSuccess: DataState()
        data class OpenServices(val list: List<MenuServiceItem>?, val item: MenuServiceItem?): DataState()
        data class OpenProducts(val list: List<MenuProductItem>?, val item: MenuProductItem?): DataState()
        data class OpenExtras(val list: List<MenuExtrasItem>?, val item: MenuExtrasItem?): DataState()
        data class OpenDelivery(val deliveryCharge: DeliveryCharge?): DataState()
        data class OpenDiscount(val discount: MenuDiscount?): DataState()
    }

//    private fun getCurrentJobOrder(customerId: UUID?) {
//        viewModelScope.launch {
//            jobOrderRepository.getCurrentJobOrder(customerId)
//        }
//    }

    private val _dataState = MutableLiveData<DataState>()
    fun dataState(): MutableLiveData<DataState> {
        return _dataState
    }
    fun resetState() {
        _dataState.value = DataState.StateLess
    }

    val jobOrderId = MutableLiveData(UUID.randomUUID())
    val jobOrderNumber = MutableLiveData("#0909776")
    val currentCustomer = MutableLiveData<CustomerMinimal>()
    val deliveryCharge = MutableLiveData<DeliveryCharge?>()
    val jobOrderServices = MutableLiveData<List<MenuServiceItem>>()
    val jobOrderProducts = MutableLiveData<MutableList<MenuProductItem>>()
    val jobOrderExtras = MutableLiveData<MutableList<MenuExtrasItem>>()
    val discount = MutableLiveData<MenuDiscount>()
    val discountInPeso = MutableLiveData(0f)

    val hasServices = MediatorLiveData<Boolean>().apply {
        fun update() {
            value = jobOrderServices.value?.size!! > 0
        }
        addSource(jobOrderServices) { update() }
    }
    val hasProducts = MediatorLiveData<Boolean>().apply {
        fun update() {
            value = jobOrderProducts.value?.size!! > 0
        }
        addSource(jobOrderProducts) { update() }
    }
    val hasExtras = MediatorLiveData<Boolean>().apply {
        fun update() {
            value = jobOrderExtras.value?.size!! > 0
        }
        addSource(jobOrderExtras) { update() }
    }
    val hasDelivery = MediatorLiveData<Boolean>().apply {
        fun update() {
            value = deliveryCharge.value != null
        }
        addSource(deliveryCharge) { update() }
    }
    val hasDiscount = MediatorLiveData<Boolean>().apply {
        fun update() {
            value = discount.value != null
        }
        addSource(discount) { update() }
    }
    val hasAny = MediatorLiveData<Boolean>().apply {
        fun update() {
            value = hasServices.value!! || hasProducts.value!! || hasExtras.value!! || hasDelivery.value!!
        }
        addSource(hasServices) { update() }
        addSource(hasProducts) { update() }
        addSource(hasExtras) { update() }
        addSource(hasDelivery) { update() }
    }

    val subtotal = MediatorLiveData<Float>().apply {
        fun update() {
            discountInPeso.value = discount.value?.let {
                var total = 0f
                total += it.getDiscount(serviceSubTotal(), DiscountApplicable.WASH_DRY_SERVICES)
                total += it.getDiscount(productSubTotal(), DiscountApplicable.PRODUCTS_CHEMICALS)
                total += it.getDiscount(extrasSubTotal(), DiscountApplicable.EXTRAS)
                total += it.getDiscount(deliveryFee(), DiscountApplicable.DELIVERY)
                total
            } ?: 0f
            value = productSubTotal() + serviceSubTotal() + extrasSubTotal() + deliveryFee() - discountInPeso.value!!
        }
        addSource(jobOrderServices) {update()}
        addSource(jobOrderProducts) {update()}
        addSource(jobOrderExtras) {update()}
        addSource(deliveryCharge) {update()}
        addSource(discount) {update()}
    }

    private fun serviceSubTotal() : Float {
        return jobOrderServices.value?.let {
            var result = 0f
            if(it.size > 0) {
                result = it.map { s -> s.price * s.quantity } .reduce { sum, element ->
                    sum + element
                }
            }
            result
        } ?: 0f
    }

    private fun productSubTotal() : Float {
        return jobOrderProducts.value?.let {
            var result = 0f
            if(it.size > 0) {
                result = it.map { s -> s.price * s.quantity } .reduce { sum, element ->
                    sum + element
                }
            }
            result
        } ?: 0f
    }

    private fun extrasSubTotal() : Float {
        return jobOrderExtras.value?.let {
            var result = 0f
            if(it.size > 0) {
                result = it.map { s -> s.price * s.quantity } .reduce { sum, element ->
                    sum + element
                }
            }
            result
        } ?: 0f
    }

    private fun deliveryFee() : Float {
        return (deliveryCharge.value?.price() ?: 0f)
    }

    fun setCustomer(customer: CustomerMinimal?) {
        currentCustomer.value = customer!!
        viewModelScope.launch {
            jobOrderRepository.getCurrentJobOrder(customer.id).let {
                if(it != null) {
                    jobOrderId.value = it.jobOrder.id
                    jobOrderNumber.value = it.jobOrder.jobOrderNumber

                    println("JOB ORDER ID IF NULL")
                    println(jobOrderId.value)

                    it.services.let { services ->
                        jobOrderServices.value = services?.map { joSvc ->
                            MenuServiceItem(joSvc.id, joSvc.serviceId, joSvc.serviceName, joSvc.serviceRef.minutes, joSvc.price, joSvc.serviceRef.machineType, joSvc.serviceRef.washType, joSvc.quantity, joSvc.used)
                        }
                    }
                } else {
                    jobOrderNumber.value = jobOrderRepository.getNextJONumber()
                    println("JOB ORDER ID IF NOT NULL")
                    println(jobOrderId.value)
                }
            }
        }
    }

    fun syncServices(services: List<MenuServiceItem>?) {
        jobOrderServices.value = services?.toList()
    }

    fun syncProducts(products: List<MenuProductItem>?) {
        jobOrderProducts.value = products?.toMutableList()
    }

    fun syncExtras(extrasItems: List<MenuExtrasItem>?) {
        jobOrderExtras.value = extrasItems?.toMutableList()
    }

    fun setDeliveryCharge(deliveryCharge: DeliveryCharge?) {
        this.deliveryCharge.value = deliveryCharge
    }

    fun applyDiscount(discount: MenuDiscount?) {
        this.discount.value = discount
    }

    fun openServices(itemPreset: MenuServiceItem?) {
        jobOrderServices.value.let {
            _dataState.value = DataState.OpenServices(it, itemPreset)
        }
    }

    fun openProducts(itemPreset: MenuProductItem?) {
        jobOrderProducts.value.let {
            _dataState.value = DataState.OpenProducts(it, itemPreset)
        }
    }

    fun openExtras(itemPreset: MenuExtrasItem?) {
        jobOrderExtras.value.let {
            _dataState.value = DataState.OpenExtras(it, itemPreset)
        }
    }

    fun openDelivery() {
        deliveryCharge.value.let {
            _dataState.value = DataState.OpenDelivery(it)
        }
    }

    fun openDiscount() {
        discount.value.let {
            _dataState.value = DataState.OpenDiscount(it)
        }
    }

    fun save() {
        viewModelScope.launch {
            val jobOrderNumber = jobOrderNumber.value
            val customerName = currentCustomer.value?.name
            val customerId = currentCustomer.value?.id
            val preparedBy = "Some staff"
            val jobOrder = EntityJobOrder(jobOrderNumber, customerId, customerName, preparedBy).apply {
                id = jobOrderId.value!!
            }
            val services = jobOrderServices.value?.map {
                EntityJobOrderService(jobOrder.id, it.serviceId, it.name, it.price, it.quantity, EntityServiceRef(it.machineType, it.washType, it.minutes), it.entityId)
            }
            val products = jobOrderProducts.value?.map {
                EntityJobOrderProduct(jobOrder.id, it.productType, it.id, it.name, it.price, it.quantity)
            }
            val jobOrderWithItem = EntityJobOrderWithItems(jobOrder, services, products)
            jobOrderRepository.save(jobOrderWithItem)
            _dataState.value = DataState.SaveSuccess
        }
    }
}