package com.csi.palabakosys.app.joborders.create

import androidx.lifecycle.*
import com.csi.palabakosys.app.customers.CustomerMinimal
import com.csi.palabakosys.app.joborders.create.delivery.DeliveryCharge
import com.csi.palabakosys.app.joborders.create.discount.MenuDiscount
import com.csi.palabakosys.app.joborders.create.extras.MenuExtrasItem
import com.csi.palabakosys.app.joborders.create.products.MenuProductItem
import com.csi.palabakosys.app.joborders.create.services.MenuServiceItem
import com.csi.palabakosys.app.joborders.payment.JobOrderPaymentMinimal
import com.csi.palabakosys.model.EnumDiscountApplicable
import com.csi.palabakosys.room.entities.*
import com.csi.palabakosys.room.repository.JobOrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class CreateJobOrderViewModel

@Inject
constructor(
    private val jobOrderRepository: JobOrderRepository
) : ViewModel() {
    sealed class DataState {
        object StateLess: DataState()
        data class SaveSuccess(val jobOrderId: UUID, val customerId: UUID): DataState()
        data class OpenServices(val list: List<MenuServiceItem>?, val item: MenuServiceItem?): DataState()
        data class OpenProducts(val list: List<MenuProductItem>?, val item: MenuProductItem?): DataState()
        data class OpenExtras(val list: List<MenuExtrasItem>?, val item: MenuExtrasItem?): DataState()
        data class OpenDelivery(val deliveryCharge: DeliveryCharge?): DataState()
        data class OpenDiscount(val discount: MenuDiscount?): DataState()
        data class OpenPayment(val customerId: UUID, val jobOrderId: UUID) : DataState()
        data class InvalidOperation(val message: String): DataState()
        data class RequestExit(val canExit: Boolean) : DataState()
        object ProceedToSaveJO: DataState()
    }

//    var modified = false
    private val _locked = MutableLiveData(false)
    val locked: LiveData<Boolean> = _locked

    private val _saved = MutableLiveData(false)
    val saved: LiveData<Boolean> = _saved

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
    val jobOrderProducts = MutableLiveData<List<MenuProductItem>>()
    val jobOrderExtras = MutableLiveData<List<MenuExtrasItem>>()
    val discount = MutableLiveData<MenuDiscount>()
    val unpaidJobOrders = MutableLiveData<List<JobOrderPaymentMinimal>>()

    val hasServices = MediatorLiveData<Boolean>().apply {
        fun update() {
            value = jobOrderServices.value?.filter { it.deletedAt == null }?.size!! > 0
        }
        addSource(jobOrderServices) { update() }
    }
    val hasProducts = MediatorLiveData<Boolean>().apply {
        fun update() {
            value = jobOrderProducts.value?.filter { it.deletedAt == null }?.size!! > 0
        }
        addSource(jobOrderProducts) { update() }
    }
    val hasExtras = MediatorLiveData<Boolean>().apply {
        fun update() {
            value = jobOrderExtras.value?.filter { it.deletedAt == null }?.size!! > 0
        }
        addSource(jobOrderExtras) { update() }
    }
    val hasDelivery = MediatorLiveData<Boolean>().apply {
        fun update() {
            value = deliveryCharge.value.let {
                it != null && it.deletedAt == null
            }
        }
        addSource(deliveryCharge) { update() }
    }
    val hasDiscount = MediatorLiveData<Boolean>().apply {
        fun update() {
            value = discount.value.let {
                it != null && it.deletedAt == null
            }
        }
        addSource(discount) { update() }
    }
    val discountInPeso = MediatorLiveData<Float>().apply {
        fun update() {
            value = discount.value?.let {
                if(it.deletedAt != null) return@let 0f
                var total = 0f
                total += it.getDiscount(serviceSubTotal(), EnumDiscountApplicable.WASH_DRY_SERVICES)
                total += it.getDiscount(productSubTotal(), EnumDiscountApplicable.PRODUCTS_CHEMICALS)
                total += it.getDiscount(extrasSubTotal(), EnumDiscountApplicable.EXTRAS)
                total += it.getDiscount(deliveryFee(), EnumDiscountApplicable.DELIVERY)
                total
            } ?: 0f
        }
        addSource(jobOrderServices) {update()}
        addSource(jobOrderProducts) {update()}
        addSource(jobOrderExtras) {update()}
        addSource(deliveryCharge) {update()}
        addSource(discount) {update()}
    }
    val hasAny = MediatorLiveData<Boolean>().apply {
        fun update() {
            value = hasServices.value == true || hasProducts.value == true || hasExtras.value == true || hasDelivery.value == true
        }
        addSource(hasServices) { update() }
        addSource(hasProducts) { update() }
        addSource(hasExtras) { update() }
        addSource(hasDelivery) { update() }
    }

    val subtotal = MediatorLiveData<Float>().apply {
        fun update() {
//            discountInPeso.value = discount.value?.let {
//                if(it.deletedAt != null) return@let 0f
//                var total = 0f
//                total += it.getDiscount(serviceSubTotal(), DiscountApplicable.WASH_DRY_SERVICES)
//                total += it.getDiscount(productSubTotal(), DiscountApplicable.PRODUCTS_CHEMICALS)
//                total += it.getDiscount(extrasSubTotal(), DiscountApplicable.EXTRAS)
//                total += it.getDiscount(deliveryFee(), DiscountApplicable.DELIVERY)
//                total
//            } ?: 0f
            value = productSubTotal() + serviceSubTotal() + extrasSubTotal() + deliveryFee() // - discountInPeso.value!!
        }
        addSource(jobOrderServices) {update()}
        addSource(jobOrderProducts) {update()}
        addSource(jobOrderExtras) {update()}
        addSource(deliveryCharge) {update()}
//        addSource(discount) {update()}
    }

    val discountedAmount = MediatorLiveData<Float>().apply {
        fun update() {
            val subtotal = subtotal.value ?: 0f
            val discount = discountInPeso.value ?: 0f
            value = subtotal - discount
        }

        addSource(subtotal) {update()}
        addSource(discountInPeso) {update()}
    }

    val previousBalance = MediatorLiveData<Float>().apply {
        fun update() {
            value = unpaidJobOrders.value ?. sumOf { it.discountedAmount.toDouble() }?.toFloat()
        }
        addSource(unpaidJobOrders) {update()}
    }

    private fun serviceSubTotal() : Float {
        return jobOrderServices.value?.filter { it.deletedAt == null }?.let {
            var result = 0f
            if(it.isNotEmpty()) {
                result = it.map { s -> s.price * s.quantity } .reduce { sum, element ->
                    sum + element
                }
            }
            result
        } ?: 0f
    }

    private fun productSubTotal() : Float {
        return jobOrderProducts.value?.filter { it.deletedAt == null }?.let {
            var result = 0f
            if(it.isNotEmpty()) {
                result = it.map { s -> s.price * s.quantity } .reduce { sum, element ->
                    sum + element
                }
            }
            result
        } ?: 0f
    }

    private fun extrasSubTotal() : Float {
        return jobOrderExtras.value?.filter { it.deletedAt == null }?.let {
            var result = 0f
            if(it.isNotEmpty()) {
                result = it.map { s -> s.price * s.quantity } .reduce { sum, element ->
                    sum + element
                }
            }
            result
        } ?: 0f
    }

    private fun deliveryFee() : Float {
        return deliveryCharge.value?.let {
            return if(it.deletedAt == null) {
                it.price
            } else {
                0f
            }
        } ?: 0f
//        return (deliveryCharge.value?.price ?: 0f)
    }

    fun setCustomer(customer: CustomerMinimal?) {
        currentCustomer.value = customer!!
        viewModelScope.launch {
            unpaidJobOrders.value = jobOrderRepository.getPreviousUnpaidByCustomerId(customer.id)
            jobOrderRepository.getCurrentJobOrder(customer.id).let {
                if(it != null) {
                    jobOrderId.value = it.jobOrder.id
                    jobOrderNumber.value = it.jobOrder.jobOrderNumber

                    println("JOB ORDER ID IF NULL")
                    println(jobOrderId.value)

                    it.services.let { services ->
                        jobOrderServices.value = services?.map { joSvc ->
                            MenuServiceItem(joSvc.id, joSvc.serviceId, joSvc.serviceName, joSvc.serviceRef.minutes, joSvc.price, joSvc.serviceRef.machineType, joSvc.serviceRef.washType, joSvc.quantity, joSvc.used).apply {
                                selected = true
                                deletedAt = joSvc.deletedAt
                            }
                        }
                    }
                    it.extras.let { extras ->
                        jobOrderExtras.value = extras?.map { joExtras ->
                            MenuExtrasItem(joExtras.id, joExtras.extrasId, joExtras.extrasName, joExtras.price, joExtras.category, joExtras.quantity).apply {
                                selected = true
                                deletedAt = joExtras.deletedAt
                            }
                        }
                    }
                    it.products.let { products ->
                        jobOrderProducts.value = products?.map { joPrd ->
                            MenuProductItem(joPrd.id, joPrd.productId, joPrd.productName, joPrd.price, joPrd.measureUnit, joPrd.unitPerServe, joPrd.quantity, 0, joPrd.productType).apply {
                                selected = true
                                deletedAt = joPrd.deletedAt
                            }
                        }?.toMutableList()
                    }
                    it.deliveryCharge?.let { entity ->
                        deliveryCharge.value = DeliveryCharge(
                            entity.deliveryProfileId,
                            entity.vehicle,
                            entity.distance,
                            entity.deliveryOption,
                            entity.price,
                            entity.deletedAt
                        ).apply {
                            deletedAt = entity.deletedAt
                        }
                    }
                    it.discount?.let { entity ->
                        discount.value = MenuDiscount(
                            entity.discountId,
                            entity.name,
                            entity.percentage,
                            entity.applicableToIds,
                            entity.deletedAt
                        ).apply {
                            selected = entity.deletedAt != null
                        }
                    }
                    _saved.value = true
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
        _saved.value = false
    }

    fun syncProducts(products: List<MenuProductItem>?) {
        jobOrderProducts.value = products?.toMutableList()
        _saved.value = false
    }

    fun syncExtras(extrasItems: List<MenuExtrasItem>?) {
        jobOrderExtras.value = extrasItems?.toMutableList()
        _saved.value = false
    }

    fun setDeliveryCharge(deliveryCharge: DeliveryCharge?) {
        if(deliveryCharge == null) {
            this.deliveryCharge.value = this.deliveryCharge.value?.apply {
                 this.deletedAt = Instant.now()
            }
        }
        this.deliveryCharge.value = deliveryCharge
        _saved.value = false
    }

    fun applyDiscount(discount: MenuDiscount?) {
        if(discount == null) {
            this.discount.value = this.discount.value?.apply {
                this.deletedAt = Instant.now()
            }
        } else {
            this.discount.value = discount
        }
        _saved.value = false
    }

    private fun isPaymentSaved() : Boolean {
        return if(_locked.value == true) {
            _dataState.value = DataState.InvalidOperation("Cannot modify paid Job Order")
            true
        } else false
    }

    fun openServices(itemPreset: MenuServiceItem?) {
        if(isPaymentSaved()) return
        jobOrderServices.value.let {
            _dataState.value = DataState.OpenServices(it, itemPreset)
        }
    }

    fun openProducts(itemPreset: MenuProductItem?) {
        if(isPaymentSaved()) return
        jobOrderProducts.value.let {
            _dataState.value = DataState.OpenProducts(it, itemPreset)
        }
    }

    fun openExtras(itemPreset: MenuExtrasItem?) {
        if(isPaymentSaved()) return
        jobOrderExtras.value.let {
            _dataState.value = DataState.OpenExtras(it, itemPreset)
        }
    }

    fun openDelivery() {
        if(isPaymentSaved()) return
        deliveryCharge.value.let {
            _dataState.value = DataState.OpenDelivery(it)
        }
    }

    fun openDiscount() {
        if(isPaymentSaved()) return
        discount.value.let {
            _dataState.value = DataState.OpenDiscount(it)
        }
    }

    fun openPayment() {
        if(hasAny.value != true) {
            _dataState.value = DataState.InvalidOperation("Your Job Order is empty!")
            return
        }
        _dataState.value = DataState.OpenPayment(currentCustomer.value!!.id, jobOrderId.value!!)
    }

    fun validate() {
        if(hasAny.value != true) {
            _dataState.value = DataState.InvalidOperation("Your Job Order is empty!")
            return
        }

        _dataState.value = DataState.ProceedToSaveJO
    }

    fun requestExit() {
        val hasAny = hasAny.value ?: false
        val saved = saved.value ?: false
        _dataState.value = DataState.RequestExit(canExit = (!saved && !hasAny) || saved)
    }

    fun save(userId: UUID) {
        if(isPaymentSaved()) return
        viewModelScope.launch {

            val jobOrderNumber = jobOrderNumber.value
            val customerId = currentCustomer.value?.id ?: return@launch

            val subtotal = subtotal.value ?: 0f
            val discountInPeso = discountInPeso.value ?: 0f
            val discountedAmount = subtotal - discountInPeso

            val jobOrder = EntityJobOrder(jobOrderNumber, customerId, userId, subtotal, discountInPeso, discountedAmount).apply {
                id = jobOrderId.value!!
            }
            val services = jobOrderServices.value?.map {
                EntityJobOrderService(jobOrder.id, it.serviceRefId, it.name, it.price, it.quantity, it.used, EntityServiceRef(it.machineType, it.washType, it.minutes), it.joServiceItemId).apply {
                    deletedAt = it.deletedAt
                }
            }
            val products = jobOrderProducts.value?.map {
                EntityJobOrderProduct(jobOrder.id, it.productRefId, it.name, it.price, it.measureUnit, it.unitPerServe, it.quantity, it.productType, it.joProductItemId).apply {
                    deletedAt = it.deletedAt
                }
            }
            val extras = jobOrderExtras.value?.map {
                EntityJobOrderExtras(jobOrder.id, it.extrasRefId, it.name, it.price, it.quantity, it.category, it.joExtrasItemId).apply {
                    deletedAt = it.deletedAt
                }
            }
            val delivery = deliveryCharge.value?.let {
                EntityJobOrderDeliveryCharge(it.deliveryProfileId, it.vehicle, it.deliveryOption, it.price, it.distance, jobOrder.id).apply {
                    deletedAt = it.deletedAt
                }
            }
            val discount = discount.value?.let {
                EntityJobOrderDiscount(it.discountRefId, it.name, it.percentage, it.applicableToIds, jobOrder.id).apply {
                    deletedAt = it.deletedAt
                }
            }
            val jobOrderWithItem = EntityJobOrderWithItems(jobOrder, services, extras, products, delivery, discount)

            jobOrderRepository.save(jobOrderWithItem)
            _dataState.value = DataState.SaveSuccess(jobOrder.id, customerId)
            _saved.value = true
        }
    }

    fun lock() {
        _locked.value = true
    }
}