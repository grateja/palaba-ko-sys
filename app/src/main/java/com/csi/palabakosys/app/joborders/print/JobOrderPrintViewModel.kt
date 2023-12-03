package com.csi.palabakosys.app.joborders.print

import androidx.lifecycle.*
import com.csi.palabakosys.model.EnumDiscountType
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

    val shopName = dataStoreRepository.shopName
    val address = dataStoreRepository.address
    val contactNumber = dataStoreRepository.contactNumber
    val email = dataStoreRepository.email

    val services = MediatorLiveData<List<PrintItem>?>().apply {
        fun update() {
            val items = jobOrderWithItems.value
            value = items?.services?.map {
                PrintItem(
                    it.quantity, it.serviceName, it.price * it.quantity
                )
            }
        }
        addSource(jobOrderWithItems){update()}
    }

    val products = MediatorLiveData<List<PrintItem>?>().apply {
        fun update() {
            val items = jobOrderWithItems.value
            value = items?.products?.map {
                PrintItem(
                    it.quantity, it.productName, it.price * it.quantity
                )
            }
        }
        addSource(jobOrderWithItems){update()}
    }

    val extras = MediatorLiveData<List<PrintItem>?>().apply {
        fun update() {
            val items = jobOrderWithItems.value
            value = items?.extras?.map {
                PrintItem(
                    it.quantity, it.extrasName, it.price * it.quantity
                )
            }
        }
        addSource(jobOrderWithItems){update()}
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

        val date = jobOrder?.jobOrder?.let {
            "       DATE: ${it.createdAt.toShort()}\n"
        } ?: ""
        val joNumber = jobOrder?.jobOrder?.let {
            "        JO#: ${it.jobOrderNumber}\n"
        } ?: ""
        val customer = jobOrder?.let {
            "   CUSTOMER: ${it.customer?.name}\n"
        } ?: ""
        val preparedBy = jobOrder?.let {
            "PREPARED BY: ${it.user?.name}\n"
        } ?: ""

        val services = services.value?.takeIf { it.isNotEmpty() }?.let {
            "[L]<b>SERVICES</b>\n" + it.joinToString("\n") + "\n"
        } ?: ""

        val products = products.value?.takeIf { it.isNotEmpty() }?.let {
            "[L]<b>PRODUCTS</b>\n" + it.joinToString("\n") + "\n"
        } ?: ""

        val extras = extras.value?.takeIf { it.isNotEmpty() }?.let {
            "[L]<b>EXTRAS</b>\n" + it.joinToString("\n") + "\n"
        } ?: ""

        val delivery = jobOrder?.deliveryCharge?.let {
            "[L]<b>${it.deliveryOption.value}</b>\n"+
            "[L](${it.distance}KM)${it.vehicle}[R]P${it.price}\n"
        } ?: ""

        val discountInPeso = jobOrder?.discountInPeso()

        val discount = jobOrder?.discount?.let { discount ->
            val discountValue = discount.discountType.let {
                if(it == EnumDiscountType.PERCENTAGE) {
                    "(${discount.value}%)[R]-P${discountInPeso}"
                } else {
                    "[R]-P${discount.value}"
                }
            }
            "[L]<b>${discount.name} discount $discountValue</b>\n"
        } ?: ""

        val subtotal = jobOrder?.subtotal()

        val paymentDetails = jobOrder?.paymentWithUser?.payment?.run {
            val changeText = change().takeIf { it > 0 }?.let { "     CHANGE: P$it\n" } ?: ""
            "  DATE PAID: ${createdAt.toShort()}\n" +
            "PMT. METHOD: ${method()}\n" +
            "   AMT. DUE: P$amountDue\n" +
            " AMT. RECVD: P$cashReceived\n" +
            changeText +
            (entityCashless?.let { "       REF#: ${it.refNumber}\n" } ?: "") +
            (orNumber?.let { "  OR NUMBER: $it\n" } ?: "") +
            "RECEIVED BY: ${jobOrder.paymentWithUser?.user?.name}\n"
        } ?: "CURRENT BALANCE: [R]P$subtotal\n"

        val formattedText = "$shopName$address$contactInfo" +
                header +
                "$date$joNumber$customer$preparedBy" +
                singleLine +
                services +
                products +
                extras +
                delivery +
                discount +
                singleLine +
                "[L]<b>SUBTOTAL [R]P$subtotal</b>\n" +
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