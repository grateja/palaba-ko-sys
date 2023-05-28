package com.csi.palabakosys.room.repository

import androidx.lifecycle.LiveData
import com.csi.palabakosys.room.dao.DaoJobOrderPayment
import com.csi.palabakosys.room.entities.EntityJobOrderPayment
import com.csi.palabakosys.room.entities.EntityJobOrderPaymentFull
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PaymentRepository
@Inject
constructor (
    private val daoPayment: DaoJobOrderPayment,
) {
    suspend fun get(id: UUID?): EntityJobOrderPayment? {
        return daoPayment.get(id)
    }

    suspend fun getPaymentWithJobOrders(id: UUID): EntityJobOrderPaymentFull? {
        return daoPayment.getPaymentWithJobOrders(id)
    }

    suspend fun save(payment: EntityJobOrderPayment, jobOrderIds: List<UUID>) : EntityJobOrderPayment {
        return daoPayment.save(payment, jobOrderIds)
    }

    fun getCashlessProviders(): LiveData<List<String>> {
        return daoPayment.getCashlessProviders()
    }
}