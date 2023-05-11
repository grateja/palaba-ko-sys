package com.csi.palabakosys.app.joborders.payment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csi.palabakosys.model.PaymentMethodEnum
import com.csi.palabakosys.room.entities.EntityCashless
import com.csi.palabakosys.room.entities.EntityJobOrder
import com.csi.palabakosys.room.entities.EntityJobOrderPayment
import com.csi.palabakosys.room.repository.JobOrderRepository
import com.csi.palabakosys.room.repository.PaymentRepository
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
) : ViewModel() {
    sealed class DataState {
        object StateLess : DataState()
        class OpenCashless(val cashless: EntityCashless?) : DataState()
    }

    private val _dataState = MutableLiveData<DataState>()
    val dataState: LiveData<DataState> = _dataState

    val jobOrders = MutableLiveData<List<EntityJobOrder>>()
    private lateinit var paymentId: UUID

    private val _amountDue = MutableLiveData(0f)
    val amountDue: LiveData<Float> = _amountDue

    val paymentMethod = MutableLiveData(PaymentMethodEnum.CASH)
    val cashReceived = MutableLiveData("")
    val orNumber = MutableLiveData("")
    val cashless = MutableLiveData<EntityCashless?>()

    fun resetState() {
        _dataState.value = DataState.StateLess
    }

    fun getUnpaidByCustomerId(customerId: UUID) {
        viewModelScope.launch {
            jobOrderRepository.getUnpaidByCustomerId(customerId).let { jo->
                jobOrders.value = jo
                paymentId = jo.firstOrNull()?.paymentId ?: UUID.randomUUID()

                if(getPayment(paymentId) != null) {
                    return@launch
                }

                _amountDue.value = jo.sumOf { it.discountedAmount.toDouble() }.toFloat()
                cashReceived.value = _amountDue.value?.toInt().toString()
            }
        }
    }

    private suspend fun getPayment(paymentId: UUID): EntityJobOrderPayment? {
        return paymentRepository.get(paymentId)?.let {
            paymentMethod.value = it.paymentMethod
            orNumber.value = it.orNumber
            cashless.value = it.entityCashless
            _amountDue.value = it.amountDue
            it
        }
    }

    fun save() {
        viewModelScope.launch {
            val paidTo = "some staff"
            val payment = EntityJobOrderPayment(
                paymentId,
                paymentMethod.value!!,
                _amountDue.value!!,
                cashReceived.value!!.toFloat(),
                paidTo,
                orNumber.value!!,
                cashless.value
            )
            paymentRepository.save(payment, jobOrders.value!!.map { it.id })
        }
    }

    fun openCashless() {
        _dataState.value = DataState.OpenCashless(cashless.value)
    }

    fun setCashless(cashless: EntityCashless?) {
        this.cashless.value = cashless
    }
}