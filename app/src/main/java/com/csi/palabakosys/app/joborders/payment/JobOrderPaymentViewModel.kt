package com.csi.palabakosys.app.joborders.payment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csi.palabakosys.model.EnumPaymentMethod
import com.csi.palabakosys.model.Rule
import com.csi.palabakosys.preferences.AppPreferenceRepository
import com.csi.palabakosys.room.entities.EntityCashless
//import com.csi.palabakosys.room.entities.EntityCashlessProvider
import com.csi.palabakosys.room.entities.EntityJobOrderPayment
import com.csi.palabakosys.room.entities.EntityJobOrderPaymentFull
import com.csi.palabakosys.room.repository.JobOrderRepository
import com.csi.palabakosys.room.repository.PaymentRepository
import com.csi.palabakosys.util.InputValidation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class JobOrderPaymentViewModel

@Inject
constructor(
    private val jobOrderRepository: JobOrderRepository,
    private val paymentRepository: PaymentRepository,
    private val appPreferenceRepository: AppPreferenceRepository,
) : ViewModel() {
    sealed class DataState {
        object StateLess : DataState()
        class OpenCashless(val cashless: EntityCashless?) : DataState()
        class PaymentSuccess(val payment: EntityJobOrderPayment, val jobOrderIds: ArrayList<String>) : DataState()
        class InvalidInput(val inputValidation: InputValidation) : DataState()
        class InvalidOperation(val message: String) : DataState()
        object ValidationPassed: DataState()
    }

    val paymentMethod = MutableLiveData(EnumPaymentMethod.CASH)
    val cashReceived = MutableLiveData("")
    val cashlessAmount = MutableLiveData("")
    val cashlessRefNumber = MutableLiveData("")
    val cashlessProvider = MutableLiveData("")
    val orNumber = MutableLiveData("")
    val cashlessProviders = paymentRepository.getCashlessProviders()
//    val cashless = MutableLiveData<EntityCashless?>()

    private val _payment = MutableLiveData<EntityJobOrderPaymentFull>()
    val payment: LiveData<EntityJobOrderPaymentFull> = _payment

    private val _inputValidation = MutableLiveData(InputValidation())
    val inputValidation: LiveData<InputValidation> = _inputValidation

    private val _dataState = MutableLiveData<DataState>()
    val dataState: LiveData<DataState> = _dataState

    private val _payableJobOrders = MutableLiveData<List<JobOrderPaymentMinimal>>()
    val payableJobOrders: LiveData<List<JobOrderPaymentMinimal>> = _payableJobOrders

    val payableAmount = MediatorLiveData<Float>().apply {
        fun update() {
            value = payableJobOrders.value?.sumOf { it.discountedAmount.toDouble() }?.toFloat() ?: 0f
        }
        addSource(payableJobOrders) {update()}
    }

    val amountToPay = MediatorLiveData<Float>().apply {
        fun update() {
            value = payableJobOrders.value?.filter{ it.selected }?.sumOf { it.discountedAmount.toDouble() }?.toFloat() ?: 0f
        }
        addSource(payableJobOrders) {update()}
    }

    val remainingBalance = MediatorLiveData<Float>().apply {
        fun update() {
            value = payableJobOrders.value?.filter{ !it.selected }?.sumOf { it.discountedAmount.toDouble() }?.toFloat() ?: 0f
        }
        addSource(payableJobOrders) {update()}
    }

    val change = MediatorLiveData<Float>().apply {
        fun update() {
            val cash = cashReceived.value?.toFloatOrNull() ?: 0f
            val amountToPay = amountToPay.value ?: 0f
            val change = cash - amountToPay
            value = if(change >= 0 && amountToPay > 0) { change } else { 0f }
        }
        addSource(cashReceived){update()}
        addSource(amountToPay){update()}
    }

//    private lateinit var paymentId: UUID

//    private val _amountDue = MutableLiveData(0f)
//    val amountDue: LiveData<Float> = _amountDue

    fun resetState() {
        _dataState.value = DataState.StateLess
    }

    fun getPayment(paymentId: UUID) {
        viewModelScope.launch {
            paymentRepository.getPaymentWithJobOrders(paymentId)?.let {
                _payment.value = it
            }
        }
    }

    fun getUnpaidByCustomerId(customerId: UUID?) {
        viewModelScope.launch {
            jobOrderRepository.getAllUnpaidByCustomerId(customerId).let { jo->
                _payableJobOrders.value = jo
//                paymentId = jo.firstOrNull()?.paymentId ?: UUID.randomUUID()

//                if(getPayment(paymentId) != null) {
//                    return@launch
//                }

//                _amountDue.value = jo.sumOf { it.discountedAmount.toDouble() }.toFloat()
//                cashReceived.value = _amountDue.value?.toInt().toString()
            }
        }
    }

//    private suspend fun getPayment(paymentId: UUID): EntityJobOrderPayment? {
//        return paymentRepository.get(paymentId)?.let {
//            paymentMethod.value = it.paymentMethod
//            orNumber.value = it.orNumber
//            cashless.value = it.entityCashless
//            _amountDue.value = it.amountDue
//            it
//        }
//    }

    fun clearError(key: String) {
        _inputValidation.value = _inputValidation.value?.removeError(key)
    }

    fun validate() {
        val validation = InputValidation()
        if(appPreferenceRepository.requireORNumber()) {
            validation.addRule("orNumber", orNumber.value, arrayOf(Rule.Required))
        }

        _payableJobOrders.value?.let { list ->
            if(!list.any {it.selected}) {
                _dataState.value = DataState.InvalidOperation("No selected Job Order")
                return
            }
        }

        if(paymentMethod.value == EnumPaymentMethod.CASH) {
            validation.addRule(
                "cashReceived",
                cashReceived.value,
                arrayOf(
                    Rule.Required,
                    Rule.IsNumeric,
                    Rule.Min(amountToPay.value, "The payment amount is insufficient.")
                )
            )
        } else if(paymentMethod.value == EnumPaymentMethod.CASHLESS) {
            validation.addRule(
                "cashlessAmount",
                cashlessAmount.value,
                arrayOf(
                    Rule.Required,
                    Rule.IsNumeric,
                    Rule.Min(amountToPay.value, "The payment amount is insufficient."),
                    Rule.ExactAmount(amountToPay.value, "Exact amount only")
                )
            )
            validation.addRule(
                "cashlessRefNumber",
                cashlessRefNumber.value,
                arrayOf(
                    Rule.Required
                )
            )
        }


        if(validation.isInvalid()) {
            _dataState.value = DataState.InvalidInput(validation)
        } else {
            _dataState.value = DataState.ValidationPassed
        }
        _inputValidation.value = validation
    }

    fun save(userId: UUID) {
        viewModelScope.launch {
            val cashless = if(paymentMethod.value == EnumPaymentMethod.CASHLESS) {
                cashReceived.value = ""
                EntityCashless(
                    cashlessProvider.value,
                    cashlessRefNumber.value,
                    cashlessAmount.value?.toFloatOrNull()
                )
            } else null

            val payableJobOrders = payableJobOrders.value

            val jobOrderIds = payableJobOrders?.filter { it.selected }?.map { it.id } ?: return@launch

            val payment = EntityJobOrderPayment(
                UUID.randomUUID(),
                paymentMethod.value!!,
                amountToPay.value ?: 0f,
                cashReceived.value?.toFloatOrNull() ?: 0f,
                userId,
                orNumber.value,
                cashless
            )
            paymentRepository.save(payment, jobOrderIds)
            _dataState.value = DataState.PaymentSuccess(payment, ArrayList(jobOrderIds.map {it.toString()}))
        }
    }

//    fun openCashless() {
//        _dataState.value = DataState.OpenCashless(cashless.value)
//    }
//
//    fun setCashless(cashless: EntityCashless?) {
//        this.cashless.value = cashless
//    }

    fun selectItem(jobOrder: JobOrderPaymentMinimal) {
        _payableJobOrders.value = _payableJobOrders.value?.apply {
            this.find {it.id == jobOrder.id}?.selected = jobOrder.selected
        }
    }
}