package com.csi.palabakosys.app.joborders.payment.preview

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.csi.palabakosys.room.repository.PaymentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class PaymentPreviewViewModel

@Inject
constructor(
    private val paymentRepository: PaymentRepository
) : ViewModel() {
    fun setPaymentId(paymentId: UUID) {
        _paymentId.value = paymentId
    }

    private val _paymentId = MutableLiveData<UUID>()
    val payment = _paymentId.switchMap { paymentRepository.getPaymentWithJobOrders(it) }

    val jobOrders = _paymentId.switchMap { paymentRepository.getJobOrdersByPaymentId(it) }
}