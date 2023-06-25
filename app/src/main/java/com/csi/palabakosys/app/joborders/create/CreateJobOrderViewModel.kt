package com.csi.palabakosys.app.joborders.create

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import androidx.lifecycle.*
import com.csi.palabakosys.app.customers.CustomerMinimal
import com.csi.palabakosys.app.joborders.create.delivery.DeliveryCharge
import com.csi.palabakosys.app.joborders.create.discount.MenuDiscount
import com.csi.palabakosys.app.joborders.create.extras.MenuExtrasItem
import com.csi.palabakosys.app.joborders.create.products.MenuProductItem
import com.csi.palabakosys.app.joborders.create.services.MenuServiceItem
import com.csi.palabakosys.app.joborders.list.JobOrderListItem
import com.csi.palabakosys.app.joborders.payment.JobOrderPaymentMinimal
import com.csi.palabakosys.model.EnumDiscountType
import com.csi.palabakosys.model.EnumDiscountApplicable
import com.csi.palabakosys.room.entities.*
import com.csi.palabakosys.room.repository.JobOrderRepository
import com.csi.palabakosys.room.repository.PaymentRepository
import com.csi.palabakosys.util.isToday
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class CreateJobOrderViewModel

@Inject
constructor(
    private val jobOrderRepository: JobOrderRepository,
    private val paymentRepository: PaymentRepository,
//    private val packageRepository: JobOrderPackageRepository,
) : ViewModel() {
    sealed class DataState {
        object StateLess: DataState()
        data class SaveSuccess(val jobOrderId: UUID, val customerId: UUID): DataState()
        data class OpenServices(val list: List<MenuServiceItem>?, val item: MenuServiceItem?): DataState()
        object OpenPackages : DataState()
        data class OpenProducts(val list: List<MenuProductItem>?, val item: MenuProductItem?): DataState()
        data class OpenExtras(val list: List<MenuExtrasItem>?, val item: MenuExtrasItem?): DataState()
        data class OpenDelivery(val deliveryCharge: DeliveryCharge?): DataState()
        data class OpenDiscount(val discount: MenuDiscount?): DataState()
        data class OpenPayment(val customerId: UUID, val paymentId: UUID?) : DataState()
        data class InvalidOperation(val message: String): DataState()
        data class RequestExit(val canExit: Boolean, val resultCode: Int, val jobOrderId: UUID?) : DataState()
        data class RequestCancel(val jobOrderId: UUID?) : DataState()
        data class ModifyDateTime(val createdAt: Instant) : DataState()
        object ProceedToSaveJO: DataState()
    }

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

//    val currentJobOrder = MutableLiveData<EntityJobOrder>()

    private val jobOrderId = MutableLiveData<UUID>()
//    private val paymentId = MutableLiveData<UUID>()
    val createdAt = MutableLiveData<Instant>()
    val jobOrderNumber = MutableLiveData("")
    val currentCustomer = MutableLiveData<CustomerMinimal>()
    val deliveryCharge = MutableLiveData<DeliveryCharge?>()
    val jobOrderServices = MutableLiveData<List<MenuServiceItem>>()
    val jobOrderProducts = MutableLiveData<List<MenuProductItem>>()
    val jobOrderExtras = MutableLiveData<List<MenuExtrasItem>>()
    val discount = MutableLiveData<MenuDiscount>()
    val unpaidJobOrders = MutableLiveData<List<JobOrderPaymentMinimal>>()
//    val createdAt = MutableLiveData(Instant.now())

    private val _payment = MutableLiveData<EntityJobOrderPayment>()
    val payment: LiveData<EntityJobOrderPayment> = _payment

    /** region mediator live data */

//    val hasPackages = MediatorLiveData<Boolean>().apply {
//        fun update() {
//            value = jobOrderPackage.value?.filter {it.deletedAt == null}?.size!! > 0
//        }
//        addSource(jobOrderPackage) {update()}
//    }
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
                if(it.discountType == EnumDiscountType.FIXED) return@let it.value
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
            value = productSubTotal() + serviceSubTotal() + extrasSubTotal() + deliveryFee() // - discountInPeso.value!!
        }
        addSource(jobOrderServices) {update()}
        addSource(jobOrderProducts) {update()}
        addSource(jobOrderExtras) {update()}
        addSource(deliveryCharge) {update()}
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
            val jobOrderId = jobOrderId.value
            value = unpaidJobOrders.value ?.filter { it.id != jobOrderId } ?. sumOf { it.discountedAmount.toDouble() }?.toFloat()
        }
        addSource(unpaidJobOrders) {update()}
    }

    val totalAmountDue = MediatorLiveData<Float>().apply {
        fun update() {
            val discountedAmount = discountedAmount.value ?: 0f
            val previousBalance = previousBalance.value ?: 0f
            value = discountedAmount + previousBalance
        }

        addSource(discountedAmount) {update()}
        addSource(previousBalance) {update()}
    }

    val hasPreviousBalance = MediatorLiveData<Boolean>().apply {
        fun update() {
            value = unpaidJobOrders.value?.isNotEmpty()
        }
        addSource(unpaidJobOrders) {update()}
    }

    /** endregion mediator live data */

    /** region computed functions */

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
    }

    private fun isLocked() : Boolean {
        return if(_locked.value == true) {
            _dataState.value = DataState.InvalidOperation("Cannot modify locked Job Order")
            true
        } else false
    }

    /** endregion */

    /** region setter functions */

    private suspend fun loadUnpaidJobOrders(customerId: UUID/*, jobOrderId: UUID?*/) : Boolean {
        val unpaid = jobOrderRepository.getAllUnpaidByCustomerId(customerId)
        unpaidJobOrders.value = unpaid
        return unpaid.isNotEmpty()
    }

    fun loadPayment() {
        viewModelScope.launch {
            val jobOrderId = jobOrderId.value
            paymentRepository.getByJobOrderId(jobOrderId)?.let {
                _payment.value = it
                _locked.value = true
            }
        }
    }

    private fun prepare(jobOrder: EntityJobOrderWithItems) {
        jobOrder.services?.let { services ->
            jobOrderServices.value = services.map { joSvc ->
                MenuServiceItem(
                    joSvc.id,
                    joSvc.serviceId,
                    joSvc.serviceName,
                    joSvc.serviceRef.minutes,
                    joSvc.price,
                    joSvc.serviceRef.machineType,
                    joSvc.serviceRef.washType,
                    joSvc.quantity,
                    joSvc.used,
                    joSvc.deletedAt).apply {
                    selected = true
                }
            }
        }
        jobOrder.extras?.let { extras ->
            jobOrderExtras.value = extras.map { joExtras ->
                MenuExtrasItem(
                    joExtras.id,
                    joExtras.extrasId,
                    joExtras.extrasName,
                    joExtras.price,
                    joExtras.category,
                    joExtras.quantity,
                    joExtras.deletedAt).apply {
                    selected = true
                }
            }
        }
        jobOrder.products?.let { products ->
            jobOrderProducts.value = products.map { joPrd ->
                MenuProductItem(
                    joPrd.id,
                    joPrd.productId,
                    joPrd.productName,
                    joPrd.price,
                    joPrd.measureUnit,
                    joPrd.unitPerServe,
                    joPrd.quantity,
                    0,
                    joPrd.productType,
                    joPrd.deletedAt).apply {
                    selected = true
                }
            }
        }
        jobOrder.deliveryCharge?.let { entity ->
            deliveryCharge.value = DeliveryCharge(
                entity.deliveryProfileId,
                entity.vehicle,
                entity.distance,
                entity.deliveryOption,
                entity.price,
                entity.deletedAt,
            )
        }
        jobOrder.discount?.let { entity ->
            discount.value = MenuDiscount(
                entity.discountId,
                entity.name,
                entity.value,
                entity.applicableToIds,
                entity.discountType,
                entity.deletedAt,
            )
        }
        jobOrder.payment?.let {
            _locked.value = true
            _payment.value = it
        }
        if(!jobOrder.jobOrder.createdAt.isToday()) {
            _locked.value = true
        }
        _saved.value = true
    }

    fun setJobOrder(joId: UUID) {
        if(currentCustomer.value != null) return
        viewModelScope.launch {
            jobOrderRepository.getJobOrderWithItems(joId).let {
                jobOrderId.value = it?.jobOrder?.id
                createdAt.value = it?.jobOrder?.createdAt
                jobOrderNumber.value = it?.jobOrder?.jobOrderNumber
                if(it != null) {
                    currentCustomer.value = CustomerMinimal(
                        it.customer?.id!!, it.customer?.name!!, it.customer?.crn!!, it.customer?.address, null
                    )
                    prepare(it)
                    loadUnpaidJobOrders(it.customer?.id!!/*, it.jobOrder.id*/)
                } else {
                    _dataState.value = DataState.InvalidOperation("Job Order maybe deleted")
                }
            }
        }
    }

    fun setCustomer(customer: CustomerMinimal) {
        if(currentCustomer.value != null) return
        currentCustomer.value = customer
        viewModelScope.launch {
            jobOrderRepository.getCurrentJobOrder(customer.id).let { jobOrderWithItems ->
                jobOrderId.value = jobOrderWithItems?.jobOrder?.id ?: UUID.randomUUID()
                createdAt.value = jobOrderWithItems?.jobOrder?.createdAt ?: Instant.now()
                jobOrderNumber.value = jobOrderWithItems?.jobOrder?.jobOrderNumber ?: jobOrderRepository.getNextJONumber()
                loadUnpaidJobOrders(customer.id)
                if(jobOrderWithItems != null) {
                    prepare(jobOrderWithItems)
                } else {
                    _dataState.value = DataState.OpenPackages
                }
            }
        }
    }

//    fun syncPackages(packages: List<MenuJobOrderPackage>?) {
//        jobOrderPackage.value = packages?.toList()
//        _saved.value = false
//        viewModelScope.launch {
//            packageRepository.getByIds(packages?.map { it.packageRefId }).forEach { packageWithItems ->
//                val services = mutableListOf<MenuServiceItem>()
//                val currentPackage = packages?.find { it.packageRefId == packageWithItems.prePackage.id }!!
//                packageWithItems.services?.forEach { eps ->
//                    val savedPackage = jobOrderServices.value?.find {
//                        it.joServiceItemId != null &&
//                        it.serviceRefId == eps.serviceId &&
//                        it.packageId != null
//                    }
//                    services.add(
//                        MenuServiceItem(
//                            savedPackage?.joServiceItemId,
//                            eps.serviceId,
//                            eps.name,
//                            eps.serviceRef.minutes,
//                            eps.unitPrice,
//                            eps.serviceRef.machineType,
//                            eps.serviceRef.washType,
//                            eps.quantity * currentPackage.quantity,
//                            savedPackage?.used ?: 0,
//                            savedPackage?.deletedAt,
//                            packageWithItems.prePackage.id
//                        )
//                    )
//                }
//                syncPackageServices(services)
//            }
//        }
//    }
//
//    private fun syncPackageServices(services: List<MenuServiceItem>?) {
//        services?.let {
//            jobOrderServices.value = it.toList()
//            _saved.value = false
//        }
//    }

    fun syncServices(services: List<MenuServiceItem>?) {
        services?.let {
            jobOrderServices.value = it.toList()
            _saved.value = false
        }
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

    fun applyDateTime(instant: Instant) {
        createdAt.value = instant
        _saved.value = false
    }

    /** endregion */

    /** region navigation */
//    fun openPackages(itemPreset: MenuJobOrderPackage?) {
//        if(isPaymentSaved()) return
//        jobOrderPackage.value.let {
//            _dataState.value = DataState.OpenPackages(it, itemPreset)
//        }
//    }

    fun requestModifyDateTime() {
        if(isLocked()) return
        createdAt.value?.let {
            _dataState.value = DataState.ModifyDateTime(it)
        }
//        currentJobOrder.value?.createdAt?.let {
//            _dataState.value = DataState.ModifyDateTime(it)
//        }
    }

    fun openServices(itemPreset: MenuServiceItem?) {
        if(isLocked()) return
        jobOrderServices.value.let {
            _dataState.value = DataState.OpenServices(it, itemPreset)
        }
    }

    fun openProducts(itemPreset: MenuProductItem?) {
        if(isLocked()) return
        jobOrderProducts.value.let {
            _dataState.value = DataState.OpenProducts(it, itemPreset)
        }
    }

    fun openExtras(itemPreset: MenuExtrasItem?) {
        if(isLocked()) return
        jobOrderExtras.value.let {
            _dataState.value = DataState.OpenExtras(it, itemPreset)
        }
    }

    fun openDelivery() {
        if(isLocked()) return
        deliveryCharge.value.let {
            _dataState.value = DataState.OpenDelivery(it)
        }
    }

    fun openDiscount() {
        if(isLocked()) return
        discount.value.let {
            _dataState.value = DataState.OpenDiscount(it)
        }
    }

    fun openPayment() {
        if(hasAny.value != true) {
            _dataState.value = DataState.InvalidOperation("Your Job Order is empty!")
            return
        }
        if(saved.value != true) {
            _dataState.value = DataState.InvalidOperation("The Job Order has not been saved yet!")
        }
        _dataState.value = DataState.OpenPayment(currentCustomer.value!!.id, payment.value?.id)
    }

//    fun openUnpaidJobOrdersPayment() {
//        _dataState.value = DataState.OpenPayment(currentCustomer.value!!.id, payment.value?.id)
//    }

    fun validate() {
        if(hasAny.value != true) {
            _dataState.value = DataState.InvalidOperation("Your Job Order is empty!")
            return
        }

        if(createdAt.value?.isAfter(Instant.now()) == true) {
            _dataState.value = DataState.InvalidOperation("Invalid date and time!")
            return
        }

        _dataState.value = DataState.ProceedToSaveJO
    }

    fun requestCancel() {
        _dataState.value = DataState.RequestCancel(jobOrderId.value)
    }

    fun requestExit() {
        val hasAny = hasAny.value ?: false
        val saved = saved.value ?: false
        val jobOrderId = jobOrderId.value
        val resultCode = if(saved) { RESULT_OK } else { RESULT_CANCELED }
        _dataState.value = DataState.RequestExit(
            (!saved && !hasAny) || saved,
            resultCode,
            jobOrderId
        )
    }

    fun unlock() {
        _locked.value = false
    }

    /** endregion */

    fun save(userId: UUID) {
        if(isLocked()) return
        viewModelScope.launch {

//            val currentJobOrder = currentJobOrder.value

            val jobOrderNumber = jobOrderNumber.value
            val customerId = currentCustomer.value?.id ?: return@launch

            val subtotal = subtotal.value ?: 0f
            val discountInPeso = discountInPeso.value ?: 0f
            val discountedAmount = subtotal - discountInPeso
            val createdAt = createdAt.value!!
            val jobOrderId = jobOrderId.value!!
            val paymentId = payment.value?.id

            val jobOrderServices = jobOrderServices.value
            val jobOrderProducts = jobOrderProducts.value
            val jobOrderExtras = jobOrderExtras.value
            val deliveryCharge = deliveryCharge.value
            val jobOrderDiscount = discount.value

            val jobOrder = EntityJobOrder(
                jobOrderNumber,
                customerId,
                userId,
                subtotal,
                discountInPeso,
                discountedAmount,
                paymentId
            ).apply {
                this.id = jobOrderId
                this.createdAt = createdAt
            }

            val services = jobOrderServices?.map {
                EntityJobOrderService(
                    jobOrder.id,
                    it.serviceRefId,
                    it.name,
                    it.price,
                    it.quantity,
                    it.used,
                    EntityServiceRef(
                        it.machineType,
                        it.washType,
                        it.minutes
                    ),
                    it.joServiceItemId ?: UUID.randomUUID()
                ).apply {
                    deletedAt = it.deletedAt
                }
            }

            val products = jobOrderProducts?.map {
                EntityJobOrderProduct(
                    jobOrder.id,
                    it.productRefId,
                    it.name,
                    it.price,
                    it.measureUnit,
                    it.unitPerServe,
                    it.quantity,
                    it.productType,
                    it.joProductItemId ?: UUID.randomUUID()
                ).apply {
                    deletedAt = it.deletedAt
                }
            }
            val extras = jobOrderExtras?.map {
                EntityJobOrderExtras(
                    jobOrder.id,
                    it.extrasRefId,
                    it.name,
                    it.price,
                    it.quantity,
                    it.category,
                    it.joExtrasItemId ?: UUID.randomUUID()
                ).apply {
                    deletedAt = it.deletedAt
                }
            }
            val delivery = deliveryCharge?.let {
                EntityJobOrderDeliveryCharge(
                    it.deliveryProfileId,
                    it.vehicle,
                    it.deliveryOption,
                    it.price,
                    it.distance,
                    jobOrder.id
                ).apply {
                    deletedAt = it.deletedAt
                }
            }
            val discount = jobOrderDiscount?.let {
                EntityJobOrderDiscount(
                    it.discountRefId,
                    it.name,
                    it.value,
                    it.discountType,
                    it.applicableToIds,
                    jobOrder.id
                ).apply {
                    deletedAt = it.deletedAt
                }
            }
            val jobOrderWithItem = EntityJobOrderWithItems(jobOrder, services, extras, products, delivery, discount)

            jobOrderRepository.save(jobOrderWithItem)
            _dataState.value = DataState.SaveSuccess(jobOrder.id, customerId)
            _saved.value = true

            prepare(jobOrderWithItem)
        }
    }
}