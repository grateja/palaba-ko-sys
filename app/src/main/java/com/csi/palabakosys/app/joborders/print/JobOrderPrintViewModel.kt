package com.csi.palabakosys.app.joborders.print

import androidx.lifecycle.*
import com.csi.palabakosys.model.EnumDiscountType
import com.csi.palabakosys.model.EnumPaymentMethod
import com.csi.palabakosys.model.PrintItem
import com.csi.palabakosys.room.repository.DataStoreRepository
import com.csi.palabakosys.room.repository.JobOrderRepository
import com.csi.palabakosys.util.toShort
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class JobOrderPrintViewModel

@Inject
constructor(
    private val jobOrderRepository: JobOrderRepository,
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {
    private val _dataState = MutableLiveData<DataState>()
    val dataState: LiveData<DataState> = _dataState

    private val _selectedTab = MutableLiveData<String>()
    val selectedTab: LiveData<String> = _selectedTab

    fun selectTab(tab: String) {
        _selectedTab.value = tab
    }

    private val _jobOrderId = MutableLiveData<UUID>()

    val jobOrderWithItems = _jobOrderId.switchMap { jobOrderRepository.getJobOrderWithItemsAsLiveData(it) }
    val unpaidJobOrders = jobOrderWithItems.switchMap { jobOrderRepository.getAllUnpaidByCustomerIdAsLiveData(it?.customer?.id) }

    val shopName = dataStoreRepository.shopName
    val address = dataStoreRepository.address
    val contactNumber = dataStoreRepository.contactNumber
    val email = dataStoreRepository.email
    val disclaimer = dataStoreRepository.jobOrderDisclaimer

    val joDetails = MediatorLiveData<List<PrintItem>>().apply {
        fun update() {
            val jobOrder = jobOrderWithItems.value
            value = listOf(
                PrintItem("       DATE", jobOrder?.jobOrder?.createdAt?.toShort()),
                PrintItem("        JO#", jobOrder?.jobOrder?.jobOrderNumber),
                PrintItem("   CUSTOMER", jobOrder?.customer?.name),
                PrintItem("PREPARED BY", jobOrder?.user?.name),
            )
        }
        addSource(jobOrderWithItems) {update()}
    }

    val items = MediatorLiveData<List<PrintItem>>().apply {
        addSource(jobOrderWithItems) {
            val jobOrder = jobOrderWithItems.value

            val services = jobOrder?.services?.takeIf { it.isNotEmpty() }?.map {
                PrintItem(it.quantity, it.serviceName, it.price * it.quantity)
            }?.let { listOf(PrintItem("SERVICES")) + it } ?: emptyList()

            val products = jobOrder?.products?.takeIf { it.isNotEmpty() } ?.map {
                PrintItem(it.quantity, it.productName, it.price * it.quantity)
            }?.let { listOf(PrintItem("PRODUCTS")) + it } ?: emptyList()

            val extras = jobOrder?.extras?.takeIf { it.isNotEmpty() }?.map {
                PrintItem(it.quantity, it.extrasName, it.price * it.quantity)
            }?.let { listOf(PrintItem("EXTRAS")) + it } ?: emptyList()

            val delivery = jobOrder?.deliveryCharge?.let {
                listOf(
                    PrintItem(it.deliveryOption.value),
                    PrintItem(null, "(${it.distance}KM)${it.vehicle.value}", it.price)
                )
            } ?: emptyList()

            value = services + products + extras + delivery
        }
    }

    val summary = MediatorLiveData<List<PrintItem>>().apply {
        addSource(jobOrderWithItems) {
            val jobOrder = jobOrderWithItems.value

            val subtotal = jobOrder?.subtotal() ?: 0.0f
            val discountInPeso = jobOrder?.discountInPeso() ?: 0.0f

            val discount = jobOrder?.discount?.let { discount ->
                PrintItem(null, "${discount.name} discount", discountInPeso)
            }

            val items = mutableListOf<PrintItem>()
            if(discount != null) {
                items.add(PrintItem(null, "SUBTOTAL" , subtotal))
                items.add(discount)
            }

            items.add(PrintItem(null, "TOTAL", subtotal - discountInPeso))

            value = items
        }
    }

    val totalAmountDue = MediatorLiveData<PrintItem>().apply {
        addSource(jobOrderWithItems) {
            val subtotal = jobOrderWithItems.value?.subtotal() ?: 0.0f
            val discountInPeso = jobOrderWithItems.value?.discountInPeso() ?: 0.0f

            val title = if (discountInPeso > 0) "DISCOUNTED AMOUNT" else "TOTAL"
            val total = subtotal - discountInPeso

            value = PrintItem(null, title, total)
        }
    }

    val services = MediatorLiveData<List<PrintItem>>().apply {
        fun update() {
            val items = jobOrderWithItems.value?.services?.map {
                PrintItem(
                    it.quantity, it.serviceName, it.price * it.quantity
                )
            }
            if(!items.isNullOrEmpty()) {
                value = listOf(PrintItem("SERVICES")) + items
            }
        }
        addSource(jobOrderWithItems){update()}
    }

    val products = MediatorLiveData<List<PrintItem>>().apply {
        fun update() {
            val items = jobOrderWithItems.value?.products?.map {
                PrintItem(
                    it.quantity, it.productName, it.price * it.quantity
                )
            }
            if(!items.isNullOrEmpty()) {
                value = listOf(PrintItem("PRODUCTS")) + items
            }
        }
        addSource(jobOrderWithItems){update()}
    }

    val extras = MediatorLiveData<List<PrintItem>?>().apply {
        fun update() {
            val items = jobOrderWithItems.value?.extras?.map {
                PrintItem(
                    it.quantity, it.extrasName, it.price * it.quantity
                )
            }
            if(!items.isNullOrEmpty()) {
                value = listOf(PrintItem("EXTRAS")) + items
            }
        }
        addSource(jobOrderWithItems){update()}
    }

    val unpaid = MediatorLiveData<List<PrintItem>?>().apply {
        fun update() {
            val jobOrderId = _jobOrderId.value

            value = listOf(PrintItem("UNPAID JOB ORDERS")) + (unpaidJobOrders.value?.filter {
                it.id != jobOrderId
            }?.map {
                PrintItem(null, it.jobOrderNumber, it.discountedAmount)
            } ?: emptyList())
        }
        addSource(unpaidJobOrders) {update()}
    }

    val hasServices = MediatorLiveData<Boolean>().apply {
        fun update() {
            value = jobOrderWithItems.value?.services?.isNotEmpty()
        }
        addSource(jobOrderWithItems) {update()}
    }

    val hasProducts = MediatorLiveData<Boolean>().apply {
        fun update() {
            value = jobOrderWithItems.value?.products?.isNotEmpty()
        }
        addSource(jobOrderWithItems) {update()}
    }

    val hasExtras = MediatorLiveData<Boolean>().apply {
        fun update() {
            value = jobOrderWithItems.value?.extras?.isNotEmpty()
        }
        addSource(jobOrderWithItems) {update()}
    }

    val hasPickupAndDelivery = MediatorLiveData<Boolean>().apply {
        fun update() {
            value = jobOrderWithItems.value?.deliveryCharge != null
        }
        addSource(jobOrderWithItems) {update()}
    }

    val hasPayment = MediatorLiveData<Boolean>().apply {
        fun update() {
            value = jobOrderWithItems.value?.paymentWithUser != null
        }
        addSource(jobOrderWithItems) {update()}
    }

    val hasUnpaidJobOrders = MediatorLiveData<Boolean>().apply {
        fun update() {
            value = unpaid.value?.isNotEmpty()
        }
        addSource(unpaid) {update()}
    }

    val contactInfo = MediatorLiveData<String>().apply {
        fun update() {
            val email = email.value
            val contactNumber = contactNumber.value

            value = "$contactNumber / $email"
        }
        addSource(contactNumber) {update()}
        addSource(email) {update()}
    }

    val deliveryOption = MediatorLiveData<String>().apply {
        fun update() {
            value = jobOrderWithItems.value?.deliveryCharge?.deliveryOption?.value
        }

        addSource(jobOrderWithItems){update()}
    }

    val currentAmountDue = MediatorLiveData<String>().apply {
        fun update() {
            val jobOrder = jobOrderWithItems.value
            value = jobOrder?.discount?.let {
                // subtotal
                // discounted amount
                "[L]SUBTOTAL[R]${jobOrder.subtotal()}\n" +
                "[L]DISCOUNTED AMOUNT[R]"
            } ?: "keme"
        }
        addSource(jobOrderWithItems) {jobOrderWithItems}
    }

    val paymentDetails = MediatorLiveData<List<PrintItem>>().apply {
        fun update() {
            val items = mutableListOf<PrintItem>()
            val jobOrder = jobOrderWithItems.value
            val payment = jobOrder?.paymentWithUser?.payment

            payment.let {
                if(it != null && jobOrder != null) {
                    items.add(PrintItem("  DATE PAID", it.createdAt.toShort()))
                    items.add(PrintItem("PMT. METHOD", it.method()))
                    if (it.paymentMethod == EnumPaymentMethod.CASHLESS) {
                        items.add(PrintItem("       REF#", it.entityCashless?.refNumber))
                    }

                    it.orNumber?.let { orNumber ->
                        items.add(PrintItem("  OR NUMBER", orNumber))
                    }

                    items.add(PrintItem("RECEIVED BY", jobOrder.paymentWithUser?.user?.name))
                } else {
                    items.add(PrintItem("UNPAID"))
                }
            }

            value = items
        }

        addSource(jobOrderWithItems) { update() }
    }


//    val paymentDetails = MediatorLiveData<List<PrintItem>>().apply {
//        fun update() {
//            val items = mutableListOf<PrintItem>()
//            val jobOrder = jobOrderWithItems.value
//            val payment = jobOrder?.paymentWithUser?.payment
//
//            if(payment != null) {
//                items.add(PrintItem("  DATE PAID:", payment.createdAt.toShort()))
//                items.add(PrintItem("PMT. METHOD:", payment.method()))
//                items.add(PrintItem("   AMT. DUE:", "P${payment.amountDue}"))
//                items.add(PrintItem(" AMT. RECVD:", "P${payment.cashReceived}"))
//                if(payment.change() > 0) {
//                    items.add(PrintItem("     CHANGE:", "P${payment.change()}"))
//                }
//                if(payment.paymentMethod == EnumPaymentMethod.CASHLESS) {
//                    items.add(PrintItem("       REF#:", payment.entityCashless?.refNumber))
//                }
//                if(payment.orNumber != null) {
//                    items.add(PrintItem("  OR NUMBER:", payment.orNumber))
//                }
//                items.add(PrintItem("RECEIVED BY:", jobOrder.paymentWithUser?.user?.name))
//            }
//            value = items
//        }
//        addSource(jobOrderWithItems) {update()}
//    }

    fun setJobOrderId(id: UUID) {
        _jobOrderId.value = id
    }

    fun print(tab: String) {
        val jobOrder = jobOrderWithItems.value
        val singleLine = "-".repeat(32) + "\n"
        val header = "[C]<font size='tall'><b>*** $tab ***</b></font>\n"

        val shopName = shopName.value.let { "[C]<font size='big'><b>$it</b></font>\n" }
        val address = address.value?.let { "[C]<font size='small'>$it</font>\n" } ?: ""
        val contactNumber = contactNumber.value
        val email = email.value

        val contactInfo = when {
            !contactNumber.isNullOrBlank() && !email.isNullOrBlank() -> "[C]<font size='small'>$contactNumber/$email</font>\n"
            !contactNumber.isNullOrBlank() -> "[C]<font size='small'>$contactNumber</font>\n"
            !email.isNullOrBlank() -> "[C]<font size='small'>$email</font>\n"
            else -> ""
        }

        val joDetails = joDetails.value?.takeIf { it.isNotEmpty() }?.let {
            it.joinToString("") { it.formattedString() }
        } ?: ""

        val items = items.value.let {
            it?.joinToString ("") { it.formattedString() }
        }
//        val services = services.value?.takeIf { it.isNotEmpty() }?.let {
//            it.joinToString("") { it.formattedString() }
//        } ?: ""
//
//        val products = products.value?.takeIf { it.isNotEmpty() }?.let {
//            it.joinToString("") { it.formattedString() }
//        } ?: ""
//
//        val extras = extras.value?.takeIf { it.isNotEmpty() }?.let {
//            it.joinToString("") { it.formattedString() }
//        } ?: ""
//
//        val delivery = jobOrder?.deliveryCharge?.let {
//            "[L]<b>${it.deliveryOption.value}</b>\n"+
//            "[L](${it.distance}KM)${it.vehicle}[R]P${it.price}\n"
//        } ?: ""
//
//        val discountInPeso = jobOrder?.discountInPeso()
//
//        val subtotal = PrintItem("[L]<b>SUBTOTAL [R]P${jobOrder?.subtotal()}</b>\n")
//
//        val discount = jobOrder?.discount?.let { discount ->
//            val discountValue = discount.discountType.let {
//                if(it == EnumDiscountType.PERCENTAGE) {
//                    "(${discount.value}%)[R]-P${discountInPeso}"
//                } else {
//                    "[R]-P${discount.value}"
//                }
//            }
//            "[L]<b>${discount.name} discount $discountValue</b>\n"
//        } ?: ""

        val summary = summary.value.let {
            it?.joinToString("") { it.formattedString() }
        }

        val paymentDetails = paymentDetails.value?.takeIf { it.isNotEmpty() }?.let {
            it.joinToString("") { it.formattedString() }
        } ?: ""

//        val unpaid = unpaid.value?.takeIf { it.isNotEmpty() }?.let {
//            "[L]<b>OTHER UNPAID JOB ORDERS</b>\n" + it.joinToString("\n") + "\n" +
//                    totalAmountDue.toString()
//        } ?: ""

        val formattedText = "$shopName$address$contactInfo" +
                header +
                joDetails +
                singleLine +
                items +
                singleLine +
                summary +
                singleLine +
                paymentDetails
        // current balance if not paid
        // other job orders
        // total balance

        println(formattedText)

         _dataState.value = DataState.Submit(formattedText)
    }

    fun resetState() {
        _dataState.value = DataState.StateLess
    }

    sealed class DataState {
        object StateLess: DataState()
        class Submit(val formattedText: String): DataState()
    }
}