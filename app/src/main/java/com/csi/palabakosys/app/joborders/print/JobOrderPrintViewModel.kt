package com.csi.palabakosys.app.joborders.print

import androidx.lifecycle.*
import com.csi.palabakosys.app.app_settings.printer.browser.PrinterDevice
import com.csi.palabakosys.app.joborders.print.JobOrderPrintActivity.Companion.TAB_CLAIM_STUB
import com.csi.palabakosys.app.joborders.print.JobOrderPrintActivity.Companion.TAB_JOB_ORDER
import com.csi.palabakosys.model.EnumPaymentMethod
import com.csi.palabakosys.model.EnumPrintState
import com.csi.palabakosys.model.PrintItem
//import com.csi.palabakosys.room.repository.DataStoreRepository
import com.csi.palabakosys.room.repository.JobOrderRepository
import com.csi.palabakosys.settings.PrinterSettingsRepository
import com.csi.palabakosys.settings.ShopPreferenceSettingsRepository
import com.csi.palabakosys.util.toShort
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class JobOrderPrintViewModel

@Inject
constructor(
    private val jobOrderRepository: JobOrderRepository,
    private val shopPrefRepository: ShopPreferenceSettingsRepository,
    private val printerSettings: PrinterSettingsRepository
) : ViewModel() {
    val showJoItemized = printerSettings.showJoItemized
    val showJoPrices = printerSettings.showJoPrices
    val showClaimStubItemized = printerSettings.showClaimStubItemized
    val showClaimStubItemPrice = printerSettings.showClaimStubJoPrices
    val showDisclaimer = printerSettings.showDisclaimer
    val characterLength = printerSettings.printerCharactersPerLine

    val bluetoothEnabled = MutableLiveData(false)
    val printerName = printerSettings.printerName
//    val printerSettings = dataStoreRepository.printerSettings
    private val _printState = MutableLiveData(EnumPrintState.READY)
    val printState: LiveData<EnumPrintState> = _printState
    val buttonPrimaryAction = MediatorLiveData<String>().apply {
        addSource(_printState) {
            value = _printState.value?.let {
                when (it) {
                    EnumPrintState.STARTED -> "CANCEL"
                    EnumPrintState.ERROR -> "RETRY"
                    else -> "CONTINUE"
                }
            } ?: "CONTINUE"
        }
    }
//    val printerName = dataStoreRepository.printerName
//    val printerAddress = dataStoreRepository.printerAddress

    private val _dataState = MutableLiveData<DataState>()
    val dataState: LiveData<DataState> = _dataState

    private val _selectedTab = MutableLiveData(TAB_CLAIM_STUB)
    val selectedTab: LiveData<String> = _selectedTab

    fun selectTab(tab: String) {
        _selectedTab.value = tab
    }

    private val _jobOrderId = MutableLiveData<UUID>()

    val jobOrderWithItems = _jobOrderId.switchMap { jobOrderRepository.getJobOrderWithItemsAsLiveData(it) }
    val unpaidJobOrders = jobOrderWithItems.switchMap { jobOrderRepository.getAllUnpaidByCustomerIdAsLiveData(it?.customer?.id) }

    val shopName = shopPrefRepository.shopName
    val address = shopPrefRepository.address
    val contactNumber = shopPrefRepository.contactNumber
    val email = shopPrefRepository.email
    val disclaimer = printerSettings.jobOrderDisclaimer

//    val joDetails = MediatorLiveData<List<PrintItem>>().apply {
//        fun update() {
//            val jobOrder = jobOrderWithItems.value
//            value = listOf(
//                PrintItem("       DATE", jobOrder?.jobOrder?.createdAt?.toShort()),
//                PrintItem("        JO#", jobOrder?.jobOrder?.jobOrderNumber),
//                PrintItem("   CUSTOMER", jobOrder?.customer?.name),
//                PrintItem("PREPARED BY", jobOrder?.user?.name),
//            )
//        }
//        addSource(jobOrderWithItems) {update()}
//    }

    val items = MediatorLiveData<List<PrintItem>>().apply {
        fun update() {
            val tab = _selectedTab.value
            val itemizedJo = showJoItemized.value
            val itemizedClaimStub = showClaimStubItemized.value
            val characters = characterLength.value ?: 32
            val singleLine = PrintItem.singleLine(characters)
            val jobOrder = jobOrderWithItems.value

            val items = mutableListOf(
                PrintItem("       DATE", jobOrder?.jobOrder?.createdAt?.toShort()),
                PrintItem("        JO#", jobOrder?.jobOrder?.jobOrderNumber),
                PrintItem("   CUSTOMER", jobOrder?.customer?.name),
                PrintItem("PREPARED BY", jobOrder?.user?.name),
            )

            if((itemizedJo == true && tab == TAB_JOB_ORDER) || (itemizedClaimStub == true && tab == TAB_CLAIM_STUB)) {
                items.add(singleLine)
                items.addAll(
                    jobOrder?.services?.takeIf { it.isNotEmpty() }?.map {
                        PrintItem(it.quantity, it.serviceName, it.price * it.quantity, characters)
                    }?.let { listOf(PrintItem("SERVICES")) + it } ?: emptyList()
                )
                items.addAll(
                    jobOrder?.products?.takeIf { it.isNotEmpty() } ?.map {
                        PrintItem(it.quantity, it.productName, it.price * it.quantity, characters)
                    }?.let { listOf(PrintItem("PRODUCTS")) + it } ?: emptyList()
                )
                items.addAll(
                    jobOrder?.extras?.takeIf { it.isNotEmpty() }?.map {
                        PrintItem(it.quantity, it.extrasName, it.price * it.quantity, characters)
                    }?.let { listOf(PrintItem("EXTRAS")) + it } ?: emptyList()
                )
                items.addAll(
                    jobOrder?.deliveryCharge?.let {
                        listOf(
                            PrintItem(it.deliveryOption.value, characterLength = characters),
                            PrintItem(null, "(${it.distance}KM)${it.vehicle.value}", it.price, characters)
                        )
                    } ?: emptyList()
                )
            }
            items.add(singleLine)

            val subtotal = jobOrder?.subtotal() ?: 0.0f
            val discountInPeso = jobOrder?.discountInPeso() ?: 0.0f

            val discount = jobOrder?.discount?.let { discount ->
                PrintItem(null, "${discount.name} discount", discountInPeso)
            }
            if(discount != null) {
                items.add(PrintItem(null, "SUBTOTAL" , subtotal))
                items.add(discount)
            }

            items.add(PrintItem(null, "TOTAL", subtotal - discountInPeso))
            items.add(singleLine)

            jobOrder?.paymentWithUser?.payment.let {
                if(it != null && jobOrder != null) {
                    items.add(PrintItem("  DATE PAID", it.createdAt.toShort()))
                    items.add(PrintItem("PMT. METHOD", it.method()))
                    if (it.paymentMethod == EnumPaymentMethod.CASHLESS) {
                        items.add(PrintItem("       REF#", it.entityCashless?.refNumber))
                    }

                    it.orNumber?.takeIf { it.isNotBlank() }?.let {
                        items.add(PrintItem("  OR NUMBER", it))
                    }

                    items.add(PrintItem("RECEIVED BY", jobOrder.paymentWithUser?.user?.name))
                } else {
                    items.add(PrintItem("UNPAID"))
                }
                items.add(singleLine)
            }



//            val services = jobOrder?.services?.takeIf { it.isNotEmpty() }?.map {
//                PrintItem(it.quantity, it.serviceName, it.price * it.quantity, characters)
//            }?.let { listOf(PrintItem("SERVICES")) + it } ?: emptyList()

//            val products = jobOrder?.products?.takeIf { it.isNotEmpty() } ?.map {
//                PrintItem(it.quantity, it.productName, it.price * it.quantity, characters)
//            }?.let { listOf(PrintItem("PRODUCTS")) + it } ?: emptyList()

//            val extras = jobOrder?.extras?.takeIf { it.isNotEmpty() }?.map {
//                PrintItem(it.quantity, it.extrasName, it.price * it.quantity, characters)
//            }?.let { listOf(PrintItem("EXTRAS")) + it } ?: emptyList()

//            val delivery = jobOrder?.deliveryCharge?.let {
//                listOf(
//                    PrintItem(it.deliveryOption.value, characterLength = characters),
//                    PrintItem(null, "(${it.distance}KM)${it.vehicle.value}", it.price, characters)
//                )
//            } ?: emptyList()

            value = items // details + services + products + extras + delivery
        }

        addSource(jobOrderWithItems) {update()}
        addSource(_selectedTab) {update()}
        addSource(showJoItemized) {update()}
        addSource(showJoPrices) {update()}
        addSource(showClaimStubItemized) {update()}
        addSource(showClaimStubItemPrice) {update()}
    }

//    val summary = MediatorLiveData<List<PrintItem>>().apply {
//        addSource(jobOrderWithItems) {
//            val jobOrder = jobOrderWithItems.value
//
//            val subtotal = jobOrder?.subtotal() ?: 0.0f
//            val discountInPeso = jobOrder?.discountInPeso() ?: 0.0f
//
//            val discount = jobOrder?.discount?.let { discount ->
//                PrintItem(null, "${discount.name} discount", discountInPeso)
//            }
//
//            val items = mutableListOf<PrintItem>()
//            if(discount != null) {
//                items.add(PrintItem(null, "SUBTOTAL" , subtotal))
//                items.add(discount)
//            }
//
//            items.add(PrintItem(null, "TOTAL", subtotal - discountInPeso))
//
//            value = items
//        }
//    }

//    val totalAmountDue = MediatorLiveData<PrintItem>().apply {
//        addSource(jobOrderWithItems) {
//            val subtotal = jobOrderWithItems.value?.subtotal() ?: 0.0f
//            val discountInPeso = jobOrderWithItems.value?.discountInPeso() ?: 0.0f
//
//            val title = if (discountInPeso > 0) "DISCOUNTED AMOUNT" else "TOTAL"
//            val total = subtotal - discountInPeso
//
//            value = PrintItem(null, title, total)
//        }
//    }

//    val services = MediatorLiveData<List<PrintItem>>().apply {
//        fun update() {
//            val items = jobOrderWithItems.value?.services?.map {
//                PrintItem(
//                    it.quantity, it.serviceName, it.price * it.quantity
//                )
//            }
//            if(!items.isNullOrEmpty()) {
//                value = listOf(PrintItem("SERVICES")) + items
//            }
//        }
//        addSource(jobOrderWithItems){update()}
//    }

//    val products = MediatorLiveData<List<PrintItem>>().apply {
//        fun update() {
//            val items = jobOrderWithItems.value?.products?.map {
//                PrintItem(
//                    it.quantity, it.productName, it.price * it.quantity
//                )
//            }
//            if(!items.isNullOrEmpty()) {
//                value = listOf(PrintItem("PRODUCTS")) + items
//            }
//        }
//        addSource(jobOrderWithItems){update()}
//    }
//
//    val extras = MediatorLiveData<List<PrintItem>?>().apply {
//        fun update() {
//            val items = jobOrderWithItems.value?.extras?.map {
//                PrintItem(
//                    it.quantity, it.extrasName, it.price * it.quantity
//                )
//            }
//            if(!items.isNullOrEmpty()) {
//                value = listOf(PrintItem("EXTRAS")) + items
//            }
//        }
//        addSource(jobOrderWithItems){update()}
//    }

//    val unpaid = MediatorLiveData<List<PrintItem>?>().apply {
//        fun update() {
//            val jobOrderId = _jobOrderId.value
//
//            value = listOf(PrintItem("UNPAID JOB ORDERS")) + (unpaidJobOrders.value?.filter {
//                it.id != jobOrderId
//            }?.map {
//                PrintItem(null, it.jobOrderNumber, it.discountedAmount)
//            } ?: emptyList())
//        }
//        addSource(unpaidJobOrders) {update()}
//    }
//
//    val hasServices = MediatorLiveData<Boolean>().apply {
//        fun update() {
//            value = jobOrderWithItems.value?.services?.isNotEmpty()
//        }
//        addSource(jobOrderWithItems) {update()}
//    }
//
//    val hasProducts = MediatorLiveData<Boolean>().apply {
//        fun update() {
//            value = jobOrderWithItems.value?.products?.isNotEmpty()
//        }
//        addSource(jobOrderWithItems) {update()}
//    }
//
//    val hasExtras = MediatorLiveData<Boolean>().apply {
//        fun update() {
//            value = jobOrderWithItems.value?.extras?.isNotEmpty()
//        }
//        addSource(jobOrderWithItems) {update()}
//    }
//
//    val hasPickupAndDelivery = MediatorLiveData<Boolean>().apply {
//        fun update() {
//            value = jobOrderWithItems.value?.deliveryCharge != null
//        }
//        addSource(jobOrderWithItems) {update()}
//    }

//    val hasPayment = MediatorLiveData<Boolean>().apply {
//        fun update() {
//            value = jobOrderWithItems.value?.paymentWithUser != null
//        }
//        addSource(jobOrderWithItems) {update()}
//    }

//    val hasUnpaidJobOrders = MediatorLiveData<Boolean>().apply {
//        fun update() {
//            value = unpaid.value?.isNotEmpty()
//        }
//        addSource(unpaid) {update()}
//    }

    val contactInfo = MediatorLiveData<String>().apply {
        fun update() {
            val email = email.value
            val contactNumber = contactNumber.value

            value = "$contactNumber / $email"
        }
        addSource(contactNumber) {update()}
        addSource(email) {update()}
    }

//    val deliveryOption = MediatorLiveData<String>().apply {
//        fun update() {
//            value = jobOrderWithItems.value?.deliveryCharge?.deliveryOption?.value
//        }
//
//        addSource(jobOrderWithItems){update()}
//    }

//    val currentAmountDue = MediatorLiveData<String>().apply {
//        fun update() {
//            val jobOrder = jobOrderWithItems.value
//            value = jobOrder?.discount?.let {
//                // subtotal
//                // discounted amount
//                "[L]SUBTOTAL[R]${jobOrder.subtotal()}\n" +
//                "[L]DISCOUNTED AMOUNT[R]"
//            } ?: "keme"
//        }
//        addSource(jobOrderWithItems) {jobOrderWithItems}
//    }

//    val paymentDetails = MediatorLiveData<List<PrintItem>>().apply {
//        fun update() {
//            val items = mutableListOf<PrintItem>()
//            val jobOrder = jobOrderWithItems.value
//            val payment = jobOrder?.paymentWithUser?.payment
//
//            payment.let {
//                if(it != null && jobOrder != null) {
//                    items.add(PrintItem("  DATE PAID", it.createdAt.toShort()))
//                    items.add(PrintItem("PMT. METHOD", it.method()))
//                    if (it.paymentMethod == EnumPaymentMethod.CASHLESS) {
//                        items.add(PrintItem("       REF#", it.entityCashless?.refNumber))
//                    }
//
//                    it.orNumber?.let { orNumber ->
//                        items.add(PrintItem("  OR NUMBER", orNumber))
//                    }
//
//                    items.add(PrintItem("RECEIVED BY", jobOrder.paymentWithUser?.user?.name))
//                } else {
//                    items.add(PrintItem("UNPAID"))
//                }
//            }
//
//            value = items
//        }
//
//        addSource(jobOrderWithItems) { update() }
//    }


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
        if(_printState.value == EnumPrintState.STARTED) {
            _dataState.value = DataState.Cancel
            return
        }

        val characterLength = characterLength.value ?: 32

        val singleLine = PrintItem.singleLine(characterLength)

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

//        val joDetails = joDetails.value?.takeIf { it.isNotEmpty() }?.let {
//            it.joinToString("") { it.formattedString() }
//        }

        val items = items.value.let {
            it?.joinToString ("") { it.formattedString() }
        }

//        val summary = summary.value.let {
//            it?.joinToString("") { it.formattedString() }
//        }
//
//        val paymentDetails = paymentDetails.value?.takeIf { it.isNotEmpty() }?.let {
//            it.joinToString("") { it.formattedString() }
//        } ?: ""

        val disclaimer =  PrintItem(disclaimer.value).formattedString()

        val formattedText = "$shopName$address$contactInfo" +
//                header +
//                joDetails +
//                singleLine +
                items// +
//                singleLine +
//                summary +
//                singleLine +
//                paymentDetails +
//                disclaimer

        println(formattedText)

         _dataState.value = DataState.Submit(formattedText)
    }

    fun resetState() {
        _dataState.value = DataState.StateLess
    }

    fun setBluetoothState(state: Boolean) {
        bluetoothEnabled.value = state
    }

    fun setDevice(device: PrinterDevice?) {
        viewModelScope.launch {
            printerSettings.update(device?.deviceName, PrinterSettingsRepository.PRINTER_NAME)
            printerSettings.update(device?.macAddress, PrinterSettingsRepository.PRINTER_ADDRESS)
        }
    }

    fun setPrintState(printState: EnumPrintState) {
        _printState.value = printState
    }

    sealed class DataState {
        object StateLess: DataState()
        object Cancel: DataState()
        class Submit(val formattedText: String): DataState()
    }
}