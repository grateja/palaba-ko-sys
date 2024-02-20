package com.csi.palabakosys.app.joborders.create

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import androidx.lifecycle.*
import com.csi.palabakosys.app.gallery.picture_preview.PhotoItem
import com.csi.palabakosys.app.joborders.create.delivery.DeliveryCharge
import com.csi.palabakosys.app.joborders.create.discount.MenuDiscount
import com.csi.palabakosys.app.joborders.create.extras.MenuExtrasItem
import com.csi.palabakosys.app.joborders.create.packages.MenuJobOrderPackage
import com.csi.palabakosys.app.joborders.create.products.MenuProductItem
import com.csi.palabakosys.app.joborders.create.services.MenuServiceItem
import com.csi.palabakosys.model.EnumDiscountApplicable
import com.csi.palabakosys.room.entities.*
import com.csi.palabakosys.room.repository.CustomerRepository
import com.csi.palabakosys.room.repository.JobOrderRepository
import com.csi.palabakosys.room.repository.PaymentRepository
import com.csi.palabakosys.room.repository.ProductRepository
import com.csi.palabakosys.util.isToday
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.time.Instant
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CreateJobOrderViewModel

@Inject
constructor(
    private val jobOrderRepository: JobOrderRepository,
    private val paymentRepository: PaymentRepository,
    private val productsRepository: ProductRepository,
    private val customerRepository: CustomerRepository
) : ViewModel() {
    sealed class DataState {
        object StateLess: DataState()
        data class SaveSuccess(val jobOrderId: UUID, val customerId: UUID): DataState()
        data class OpenServices(val list: List<MenuServiceItem>?, val item: MenuServiceItem?): DataState()
        data class OpenPackages(val list: List<MenuJobOrderPackage>?) : DataState()
        data class OpenProducts(val list: List<MenuProductItem>?, val item: MenuProductItem?): DataState()
        data class OpenExtras(val list: List<MenuExtrasItem>?, val item: MenuExtrasItem?): DataState()
        data class OpenDelivery(val deliveryCharge: DeliveryCharge?): DataState()
        data class OpenDiscount(val discount: MenuDiscount?): DataState()
        data class OpenPayment(val customerId: UUID, val paymentId: UUID?) : DataState()
        data class InvalidOperation(val message: String, val errorCode: String? = null): DataState()
        data class RequestExit(val canExit: Boolean, val resultCode: Int, val jobOrderId: UUID?) : DataState()
        data class RequestCancel(val jobOrderId: UUID?) : DataState()
        data class ModifyDateTime(val createdAt: Instant) : DataState()
        data class OpenCamera(val jobOrderId: UUID) : DataState()
        data class OpenPictures(val ids: List<PhotoItem>, val position: Int) : DataState()
//        data class EditCustomer(val customerId: UUID?) : DataState()
        data class PickCustomer(val customerId: UUID?) : DataState()
        data class SearchCustomer(val customerId: UUID?) : DataState()
        data class OpenPrinter(val jobOrderId: UUID) : DataState()
        object ProceedToSaveJO: DataState()
    }

    private val _locked = MutableLiveData(false)
    val locked: LiveData<Boolean> = _locked

    private val _saved = MutableLiveData(false)
    val saved: LiveData<Boolean> = _saved

    private val _deleted = MutableLiveData(false)
    val deleted: LiveData<Boolean> = _deleted

    private val _customerId = MutableLiveData<UUID>()

//    val prompt = MediatorLiveData<String>().apply {
//        fun update() {
//            value = if (_deleted.value == true) {
//                "Important: Job orders are automatically locked if deleted. This ensures the integrity of the records and prevents further modifications to deleted job orders. Once a job order is deleted, no additional changes, additions, or removals are permitted."
//            } else if (_locked.value == true) {
//                "Note: Job Orders created on the previous days are automatically locked. Any alterations to job orders created on previous days can lead to daily sales inconsistencies. Please be aware that job orders from earlier dates are automatically locked to maintain the accuracy of the sales report. Once locked, no additions, removals, or other modifications are permitted."
//            } else {
//                null
//            }
//        }
//        addSource(_deleted) {update()}
//        addSource(_locked) {update()}
//    }

    private val _dataState = MutableLiveData<DataState>()
    val dataState: LiveData<DataState> = _dataState

    fun resetState() {
        _dataState.value = DataState.StateLess
    }

    val jobOrderId = MutableLiveData<UUID>()
    val createdAt = MutableLiveData<Instant>()
    val jobOrderNumber = MutableLiveData("")
    val currentCustomer = _customerId.switchMap { customerRepository.getCustomerAsLiveData(it) } //MutableLiveData<CustomerMinimal>()
    val deliveryCharge = MutableLiveData<DeliveryCharge?>()
    val jobOrderServices = MutableLiveData<List<MenuServiceItem>>()
    val jobOrderProducts = MutableLiveData<List<MenuProductItem>>()
    val jobOrderExtras = MutableLiveData<List<MenuExtrasItem>>()
    val discount = MutableLiveData<MenuDiscount>()
    val unpaidJobOrders = _customerId.switchMap { jobOrderRepository.getAllUnpaidByCustomerIdAsLiveData(it) }

    private val jobOrderPackages = MutableLiveData<List<MenuJobOrderPackage>?>()

    val jobOrderPictures = jobOrderId.switchMap { jobOrderRepository.getPictures(it) }

    private val _preparedBy = MutableLiveData<String>()
    val preparedBy: LiveData<String> = _preparedBy

    private val _payment = MutableLiveData<EntityJobOrderPaymentFull>()
    val payment: LiveData<EntityJobOrderPaymentFull> = _payment

//    val cannotSwitchCustomer = MediatorLiveData<Boolean>().apply {
//        addSource(jobOrderServices) {
//            value = it.find { it.used > 0 } != null
//        }
//    }

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
    val discountInPeso = MediatorLiveData<Float>().apply {
        fun update() {
            value = discount.value?.let {
                if(it.deletedAt != null) return@let 0f
//                if(it.discountType == EnumDiscountType.FIXED) return@let it.value
                var total = 0f
                total += it.calculateDiscount(serviceSubTotal(), EnumDiscountApplicable.WASH_DRY_SERVICES)
                total += it.calculateDiscount(productSubTotal(), EnumDiscountApplicable.PRODUCTS_CHEMICALS)
                total += it.calculateDiscount(extrasSubTotal(), EnumDiscountApplicable.EXTRAS)
                total += it.calculateDiscount(deliveryFee(), EnumDiscountApplicable.DELIVERY)
                maxOf(total, 0f)
            } ?: 0f
        }
        addSource(jobOrderServices) {update()}
        addSource(jobOrderProducts) {update()}
        addSource(jobOrderExtras) {update()}
        addSource(deliveryCharge) {update()}
        addSource(discount) {update()}
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

    val servicesTotal = MediatorLiveData<Float>().apply {
        fun update() {
            value = serviceSubTotal()
        }
        addSource(jobOrderServices) { update() }
    }
    val productsTotal = MediatorLiveData<Float>().apply {
        fun update() {
            value = productSubTotal()
        }
        addSource(jobOrderProducts) { update() }
    }
    val extrasTotal = MediatorLiveData<Float>().apply {
        fun update() {
            value = extrasSubTotal()
        }
        addSource(jobOrderExtras) { update() }
    }
    val confirmStr = MediatorLiveData<String>().apply {
        fun update() {
            val amount = discountedAmount.value ?: 0f

             val str = "P %s".format(NumberFormat.getNumberInstance(Locale.US).format(amount))

            value = "$str CONFIRM"
        }
        addSource(discountedAmount) {update()}
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

//    private suspend fun loadUnpaidJobOrders(customerId: UUID/*, jobOrderId: UUID?*/) : Boolean {
//        val unpaid = jobOrderRepository.getAllUnpaidByCustomerId(customerId)
//        unpaidJobOrders.value = unpaid
//        return unpaid.isNotEmpty()
//    }

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
        _preparedBy.value = jobOrder.user?.name
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
                    joSvc.isVoid,
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
                    joExtras.isVoid,
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
                    joPrd.isVoid,
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
                entity.applicableTo,
                entity.discountType,
                entity.isVoid,
                entity.deletedAt,
            )
        }
        jobOrder.paymentWithUser?.let {
            _locked.value = true
            _payment.value = it
        }
        if(!jobOrder.jobOrder.createdAt.isToday()) {
            _locked.value = true
        }
        _saved.value = true
        _deleted.value = jobOrder.jobOrder.deletedAt != null || jobOrder.jobOrder.entityJobOrderVoid != null
    }

    fun setJobOrder(joId: UUID?, forced: Boolean) {
        // if(currentCustomer.value != null) return
        if(jobOrderId.value != null && !forced) return
        viewModelScope.launch {
            jobOrderRepository.getJobOrderWithItems(joId).let {
                _customerId.value = it?.jobOrder?.customerId
                jobOrderId.value = it?.jobOrder?.id
                createdAt.value = it?.jobOrder?.createdAt
                jobOrderNumber.value = it?.jobOrder?.jobOrderNumber
                if(it != null) {
//                    currentCustomer.value = CustomerMinimal(
//                        it.customer?.id!!, it.customer?.name!!, it.customer?.crn!!, it.customer?.address, null, null
//                    )
                    prepare(it)
//                    loadUnpaidJobOrders(it.customer?.id!!/*, it.jobOrder.id*/)
                } else {
                    jobOrderId.value = UUID.randomUUID()
                    createdAt.value = Instant.now()
                    jobOrderNumber.value = jobOrderRepository.getNextJONumber()
//                    _dataState.value = DataState.InvalidOperation("Job Order maybe deleted")
                }
            }
        }
    }

    fun loadByCustomer(customerId: UUID) {
        _customerId.value = customerId
        viewModelScope.launch {
            jobOrderRepository.getCurrentJobOrder(customerId)?.let { jobOrderWithItems ->
                jobOrderId.value = jobOrderWithItems.jobOrder.id
                createdAt.value = jobOrderWithItems.jobOrder.createdAt
                jobOrderNumber.value = jobOrderWithItems.jobOrder.jobOrderNumber
//                loadUnpaidJobOrders(customerId)
//                if(jobOrderWithItems != null) {
                prepare(jobOrderWithItems)
//                }// else {
//                    _dataState.value = DataState.OpenPackages
//                }
            }
        }
    }

    fun setCustomerId(customerId: UUID) {
//        if(currentCustomer.value != null) return
        _customerId.value = customerId
        _saved.value = false
//        currentCustomer.value = customer
//        viewModelScope.launch {
//            jobOrderRepository.getCurrentJobOrder(customerId)?.let { jobOrderWithItems ->
//                jobOrderId.value = jobOrderWithItems?.jobOrder?.id ?: UUID.randomUUID()
//                createdAt.value = jobOrderWithItems?.jobOrder?.createdAt ?: Instant.now()
//                jobOrderNumber.value = jobOrderWithItems?.jobOrder?.jobOrderNumber ?: jobOrderRepository.getNextJONumber()
//                loadUnpaidJobOrders(customerId)
//                if(jobOrderWithItems != null) {
//                    prepare(jobOrderWithItems)
//                }// else {
//                    _dataState.value = DataState.OpenPackages
//                }
//            }
//        }
    }

    fun syncPackages(packages: List<MenuJobOrderPackage>?) {
        jobOrderPackages.value = packages
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

    fun removeService(id: UUID?) {
        jobOrderServices.value?.apply {
            val found = this.find { it.serviceRefId == id }

            if(found != null) {
                if(found.used > 0) {
                    _dataState.value = DataState.InvalidOperation("Cannot remove used service")
                    return@apply
                }

                if(found.joServiceItemId != null) {
                    found.deletedAt = Instant.now()
                    jobOrderServices.value = this
                } else {
                    jobOrderServices.value = this.filter { it.serviceRefId != id }
                }
                _saved.value = false
            }
        }
    }

    fun removeProduct(id: UUID?) {
        jobOrderProducts.value?.apply {
            val found = this.find { it.productRefId == id }

            if(found != null) {
                if(found.joProductItemId != null) {
                    found.deletedAt = Instant.now()
                    jobOrderProducts.value = this
                } else {
                    jobOrderProducts.value = this.filter { it.productRefId != id }
                }
                _saved.value = false
            }
        }
    }

    fun removeExtras(id: UUID?) {
        jobOrderExtras.value?.apply {
            val found = this.find { it.extrasRefId == id }

            if(found != null) {
                if(found.joExtrasItemId != null) {
                    found.deletedAt = Instant.now()
                    jobOrderExtras.value = this
                } else {
                    jobOrderExtras.value = this.filter { it.extrasRefId != id }
                }
                _saved.value = false
            }
        }
    }

    fun applyDateTime(instant: Instant) {
        if(instant.isAfter(Instant.now())) {
            _dataState.value = DataState.InvalidOperation("Date created must not be after today")
        } else {
            createdAt.value = instant
            _saved.value = false
        }
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

    fun openPackages() {
        if(isLocked()) return
        jobOrderPackages.value.let {
            _dataState.value = DataState.OpenPackages(it)
        }
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
        _dataState.value = DataState.OpenPayment(currentCustomer.value!!.id, payment.value?.payment?.id)
    }

    fun openCamera() {
        jobOrderId.value?.let {
            _dataState.value = DataState.OpenCamera(it)
        }
    }

    fun openPictures(currentId: UUID) {
        jobOrderPictures.value?.let { _list ->
            val index = _list.indexOfFirst { it.id == currentId }
            _dataState.value = DataState.OpenPictures(_list.map {
                 PhotoItem(it.id, it.createdAt)
            }, index)
        }
    }

//    fun openUnpaidJobOrdersPayment() {
//        _dataState.value = DataState.OpenPayment(currentCustomer.value!!.id, payment.value?.id)
//    }

    fun validate() {
        if(currentCustomer.value == null) {
            _dataState.value = DataState.InvalidOperation("Select customer first!", "customer")
            return
        }

        if(hasAny.value != true) {
            _dataState.value = DataState.InvalidOperation("Your Job Order is empty!")
            return
        }

        if(createdAt.value?.isAfter(Instant.now()) == true) {
            _dataState.value = DataState.InvalidOperation("Invalid date and time!")
            return
        }

        viewModelScope.launch {
            val products = jobOrderProducts.value?.filter {
                it.deletedAt == null
            }

            if(products != null && products.isNotEmpty()) {
                products.let {
                    val unavailable = productsRepository.checkAll(it)
                    if(unavailable != null) {
                        _dataState.value = DataState.InvalidOperation(unavailable)
                        return@launch
                    } else {
                        _dataState.value = DataState.ProceedToSaveJO
                    }
                }
            } else {
                _dataState.value = DataState.ProceedToSaveJO
            }
        }

//        jobOrderProducts.value?.let { list ->
//            if(list.firstOrNull { it.currentStock < it.quantity } ?.let {
//                _dataState.value = DataState.InvalidOperation("No enough stocks for ${it.name}")
//            } != null) return
//        }

//        _dataState.value = DataState.ProceedToSaveJO
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
            val jobOrderNumber = jobOrderNumber.value
            val customerId = currentCustomer.value?.id ?: return@launch

            val subtotal = subtotal.value ?: 0f
            val discountInPeso = discountInPeso.value ?: 0f
            val discountedAmount = subtotal - discountInPeso
            val createdAt = createdAt.value!!
            val jobOrderId = jobOrderId.value ?: UUID.randomUUID()
            val paymentId = payment.value?.payment?.id

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
                    it.isVoid,
                    EntityServiceRef(
                        it.machineType,
                        it.washType,
                        it.minutes
                    ),
                    it.joServiceItemId ?: UUID.randomUUID()
                ).apply {
                    deletedAt = it.deletedAt
                    this.createdAt = createdAt
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
                    it.isVoid,
                    it.joProductItemId ?: UUID.randomUUID()
                ).apply {
                    deletedAt = it.deletedAt
                    this.createdAt = createdAt
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
                    it.isVoid,
                    it.joExtrasItemId ?: UUID.randomUUID()
                ).apply {
                    deletedAt = it.deletedAt
                    this.createdAt = createdAt
                }
            }
            val delivery = deliveryCharge?.let {
                EntityJobOrderDeliveryCharge(
                    it.deliveryProfileId,
                    it.vehicle,
                    it.deliveryOption,
                    it.price,
                    it.distance,
                    it.isVoid,
                    jobOrder.id
                ).apply {
                    deletedAt = it.deletedAt
                    this.createdAt = createdAt
                }
            }
            val discount = jobOrderDiscount?.let {
                EntityJobOrderDiscount(
                    it.discountRefId,
                    it.name,
                    it.value,
                    it.discountType,
                    it.applicableTo,
                    it.isVoid,
                    jobOrder.id
                ).apply {
                    deletedAt = it.deletedAt
                    this.createdAt = createdAt
                }
            }
            val jobOrderWithItem = EntityJobOrderWithItems(jobOrder, services, extras, products, delivery, discount)

            jobOrderRepository.save(jobOrderWithItem)
            _dataState.value = DataState.SaveSuccess(jobOrder.id, customerId)
            _saved.value = true

            prepare(jobOrderWithItem)
        }
    }

    fun attachPicture(id: UUID) {
        val jobOrderId = jobOrderId.value ?: return
        viewModelScope.launch {
            val jobOrderPictures = EntityJobOrderPictures(
                jobOrderId,
                id
            )
            jobOrderRepository.attachPicture(jobOrderPictures)
        }
    }

    fun attachPictures(ids: List<UUID>) {
        val jobOrderId = jobOrderId.value ?: return
        viewModelScope.launch {
            val jobOrderPictures = ids.map {
                EntityJobOrderPictures(
                    jobOrderId,
                    it
                )
            }
            jobOrderRepository.attachPictures(jobOrderPictures)
        }
    }

    fun removePicture(uriId: UUID) {
        viewModelScope.launch {
            jobOrderRepository.removePicture(uriId)
        }
    }

    fun pickCustomer() {
        if(isLocked()) return
        val customerId = currentCustomer.value?.id
        val cannotSwitchCustomer = jobOrderServices.value?.any {
            it.used > 0
        }

        if(cannotSwitchCustomer == true) {
            _dataState.value = DataState.InvalidOperation("Cannot change customer for a Job Order with used services")
        } else {
            _dataState.value = DataState.SearchCustomer(customerId)
        }

//        val canSwitchCustomer = jobOrderServices.value?.any {
//            it.used > 0
//        }
//
//        if(canSwitchCustomer != true) {
//            _dataState.value = DataState.EditCustomer(customerId)
//        } else {
//            _dataState.value = DataState.PickCustomer(customerId)
//        }

//        currentCustomer.value?.id?.let {
//            _dataState.value = DataState.PickCustomer(it)
//        }
    }

//    fun searchCustomer() {
//        _dataState.value = DataState.SearchCustomer
//    }

//    fun editCustomer(new: Boolean) {
//        if(isLocked()) return
//        val customerId = if (new) null else currentCustomer.value?.id
//        _dataState.value = DataState.EditCustomer(customerId)
////        currentCustomer.value?.id?.let {
////            _dataState.value = DataState.EditCustomer(it)
////        }
//    }

    fun openPrinterOptions() {
        jobOrderId.value?.let {
            _dataState.value = DataState.OpenPrinter(it)
        }
    }
}