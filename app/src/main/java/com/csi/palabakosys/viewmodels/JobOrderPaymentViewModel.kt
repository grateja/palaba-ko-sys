//package com.csi.palabakosys.viewmodels
//
//import androidx.lifecycle.MediatorLiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
////import com.csi.palabakosys.adapters.RecyclerItem
////import com.csi.palabakosys.adapters.SimpleListAdapter
//import com.csi.palabakosys.model.EnumPaymentMethod
//import com.csi.palabakosys.model.Rule
//import com.csi.palabakosys.preferences.AppPreferenceRepository
//import com.csi.palabakosys.room.entities.*
////import com.csi.palabakosys.room.repository.CashlessProvidersRepository
//import com.csi.palabakosys.room.repository.JobOrderRepository
//import com.csi.palabakosys.room.repository.PaymentRepository
//import com.csi.palabakosys.util.DataState
//import com.csi.palabakosys.util.InputValidation
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.launch
//import java.util.*
//import javax.inject.Inject
//
//@HiltViewModel
//class JobOrderPaymentViewModel
//@Inject
//constructor(
//    private val preferenceRepository: AppPreferenceRepository,
//    private val repository: PaymentRepository,
//    private val jobOrderRepository: JobOrderRepository,
////    private val cashlessProviderRepository: CashlessProvidersRepository
//) : ViewModel() {
//    var jobOrderId: UUID? = null
//    var customerId: UUID? = null
//    var cashBackEarned: Float = 0f
//    var cashlessEntity: EntityCashless? = null
//    val validation = MutableLiveData(InputValidation())
//    val dataState = MutableLiveData<DataState<EntityJobOrderPayment>>()
//
////    val cashlessAdapter = Adapter<EntityCashlessProvider>(R.layout.recycler_item_cashless_provider)
//
//    val totalAmount = MutableLiveData(0f)
//    val customer = MutableLiveData<EntityCustomer>()
//
//    val discount = MutableLiveData<EntityDiscount?>()
//
//    val cashPayment = MutableLiveData("")
//    val cashBackPayment = MutableLiveData("")
//
//    val cashlessProvider = MutableLiveData("")
//    val cashlessRefNumber = MutableLiveData("")
//
//    val availableCashback = MutableLiveData(0f)
//    val paymentMethod = MutableLiveData(EnumPaymentMethod.CASH)
//
//    val amountToPay = MediatorLiveData<Float>().apply {
//        fun update() {
//            val _totalAmount = totalAmount.value?:0f
//            val _discountPercentage = discount.value?.value?:0f
//            val _discountInPeso = _discountPercentage * _totalAmount / 100f
//            value = _totalAmount - _discountInPeso
//        }
//        addSource(totalAmount) {update()}
//        addSource(discount) {update()}
//    }
//    val cashlessAmount = MediatorLiveData<Float>().apply {
//        fun update() {
//            if(paymentMethod.value == EnumPaymentMethod.CASHLESS) {
//                value = amountToPay.value?:0f
//            } else {
//                value = 0f
//            }
////            paymentMethod.value?.let {
////            }
//        }
//        addSource(amountToPay) {update()}
//        addSource(paymentMethod) {update()}
//    }
//
//    private fun pay() : Float {
//        val _cash = cashPayment.value?.toFloatOrNull()?:0f
//        val _cashless = cashlessAmount.value?:0f
//        val _cashback = cashBackPayment.value?.toFloatOrNull()?:0f
//        return _cash + _cashless + _cashback
//    }
//
//    val balance = MediatorLiveData<Float>().apply {
//        fun update() {
//            val _cash = pay() //cash.value?.toFloatOrNull()?:0f
//            val _amountToPay = amountToPay.value?:0f
//            val _balance = _amountToPay - _cash
//            value = if(_balance < 0) {
//                0f
//            } else {
//                _balance
//            }
//        }
//        addSource(cashPayment) {update()}
//        addSource(amountToPay) {update()}
//        addSource(cashlessAmount) {update()}
////        addSource(totalAmount) {update()}
////        addSource(discount) {update()}
//    }
//    val change = MediatorLiveData<Float>().apply {
//        fun update() {
//            val _cash = pay()
//            val _amountToPay = amountToPay.value?:0f
//            val _change = _cash - _amountToPay
//
//            value = if(_change < 0) {
//                0f
//            } else {
//                _change
//            }
//        }
//        addSource(cashPayment) {update()}
//        addSource(amountToPay) {update()}
////        addSource(cashlessAmount) {update()}
////        addSource(totalAmount) {update()}
////        addSource(discount) {update()}
//    }
//    val discountInPeso = MediatorLiveData<Float>().apply {
//        fun update() {
//            val _percentage = discount.value?.value ?: 0f
//            val _totalAmount = totalAmount.value?:0f
//            value = _percentage * _totalAmount / 100
//        }
//        addSource(discount) {update()}
//        addSource(totalAmount) {update()}
//    }
//
////    val serviceAdapter = SimpleListAdapter()
////    val productAdapter = SimpleListAdapter()
//
//    fun clearInput(paymentMethod: EnumPaymentMethod?) {
//        when(paymentMethod) {
//            EnumPaymentMethod.CASH -> {
//                cashPayment.value = "0"
//            }
//            EnumPaymentMethod.CASHLESS -> {
//                cashlessProvider.value = ""
//                cashlessRefNumber.value = ""
//            }
////            PaymentMethodEnum.CASH_BACK -> {
////                cashBackPayment.value = ""
////            }
//            null -> { }
//        }
//    }
////    fun getId() : String? {
////        return model.value?.id?.toString() // this.modelId?.toString()
////    }
//
//    fun clearError(key: String = "") {
//        validation.value = validation.value?.removeError(key)
//    }
//
//    fun setInput(paymentMethod: EnumPaymentMethod?) {
//        when(paymentMethod) {
//            EnumPaymentMethod.CASH -> {
//                cashPayment.value = "0"//amountToPay.value?.toString()
////                if(crudActionEnum.value == CRUDActionEnum.CREATE) {
////                } else if(crudActionEnum.value == CRUDActionEnum.UPDATE) {
////                    cash.value = model.value?.cash?.toString()
////                }
//            }
//            EnumPaymentMethod.CASHLESS -> {
//                cashlessProvider.value = ""
//                cashlessRefNumber.value = ""
////                model.value?.entityCashless.let {
////                    if(it != null) {
////                        cashlessProvider.value = it.provider
////                        cashlessAmount.value = it.amount?.toString()
////                        cashlessRefNumber.value = it.refNumber
////                    } else {
////                        cashlessProvider.value = ""
////                        cashlessAmount.value = amountToPay.value?.toString()
////                        cashlessRefNumber.value = ""
////                    }
////                }
//            }
////            PaymentMethodEnum.CASH_BACK -> {
////                cashBackPayment.value = amountToPay.value?.toString()
////            }
//            null -> { }
//        }
//    }
//
//    fun setDiscount(_discount: EntityDiscount?) {
//        discount.value = _discount
//    }
//
//    fun get(jobOrderId: String?) {
////        super.get(paymentId, EntityJobOrderPayment(UUID.fromString(jobOrderId)))
//        this.jobOrderId = UUID.fromString(jobOrderId)
//        viewModelScope.launch {
//            jobOrderRepository.getJobOrderWithItems(UUID.fromString(jobOrderId)).let {
//                if(it == null) {
//                    dataState.value = DataState.Invalidate("Job order not found or deleted")
//                } else {
////                    totalAmount.value = it.totalAmount()
////                    availableCashback.value = it.customer.cashBack
////                    customerId = it.customer.id
////                    cashBackEarned = it.cashBack()
////                    model.value = it.payment
////                    availablePoints.value = it.customer.points
//
////                    serviceAdapter.setData(it.services.map { s ->
////                        RecyclerItem(s.serviceName?:"")
////                    })
////
////                    productAdapter.setData(it.products.map { p ->
////                        RecyclerItem(p.productName?:"")
////                    })
////
////                    if(crudActionEnum.value == CRUDActionEnum.UPDATE) {
////                        cash.value = model.value?.cash?.toString()
////                    } else if(crudActionEnum.value == CRUDActionEnum.CREATE) {
////                        cashPayment.value = it.totalAmount().toString()
////                    }
//                }
//            }
//        }
//    }
//    fun getCashlessProviders() {
//        viewModelScope.launch {
////            cashlessProviderRepository.getAll("").let { _list ->
////                cashlessAdapter.setData(_list.map {RecyclerItem(it)})
////            }
//        }
//    }
//
////    fun setCashlessProvider(provider: EntityCashlessProvider) {
////        cashlessProvider.value = provider.name
////        println("Cashless provider")
////    }
//
//    fun save() {
////        var cashless : EntityCashless? = null
//        if(this.isInvalid()) {
//            return
//        }
//        viewModelScope.launch {
////            val payment = EntityJobOrderPayment(jobOrderId).apply {
////                cash = cashPayment.value?.toFloatOrNull()?:0f
////                entityCashless = cashlessEntity
////                cashBack = cashBackPayment.value?.toFloatOrNull()?:0f
////                paidTo = preferenceRepository.getUser()?.name
////            }
////            payment.paymentMethod = paymentMethod.value
////            payment.balance = balance.value?:0f
////            payment.change = change.value?:0f
////
////            discount.value?.let {
////                payment.discountName = it.name
////                payment.discountPercentage = it.percentage
////                payment.discountInPeso = discountInPeso.value?:0f
////            }
////            repository.save(payment, customerId, cashBackEarned).let {
////                dataState.value = DataState.Success(payment)
////            }
//        }
//    }
//
//    fun isInvalid() : Boolean {
//        val inputValidation = InputValidation()
//        when(paymentMethod.value) {
//            EnumPaymentMethod.CASH -> {
//                inputValidation.addRules("cashPayment", cashPayment.value, arrayOf(Rule.Required, Rule.Min(amountToPay.value, "Not enough cash")))
//            }
//            EnumPaymentMethod.CASHLESS -> {
//                inputValidation.addRules("cashlessProvider", cashlessProvider.value, arrayOf(Rule.Required))
//                inputValidation.addRules("cashlessRefNumber", cashlessRefNumber.value, arrayOf(Rule.Required))
//                inputValidation.addRules("cashlessAmount", cashlessAmount.value, arrayOf(Rule.Required))
//                cashlessEntity = EntityCashless(cashlessProvider.value, cashlessRefNumber.value, cashlessAmount.value)
//            }
////            PaymentMethodEnum.CASH_BACK -> {
////                if((availableCashback.value ?: 0f) < (amountToPay.value ?: 0f)) {
////                    inputValidation.addError("cashBackPayment", "Not enough cash back")
////                }
////                val _amountToPay = amountToPay.value?:0f
////                cashBackEarned -= _amountToPay
////            }
//            else -> {
//                println("No payment method selected")
//                return false
//            }
//        }
//        validation.value = inputValidation
//        return inputValidation.isInvalid()
//    }
//}