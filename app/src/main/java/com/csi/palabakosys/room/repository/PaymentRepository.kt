package com.csi.palabakosys.room.repository

import com.csi.palabakosys.room.dao.DaoJobOrderPayment
import com.csi.palabakosys.room.entities.EntityJobOrder
import com.csi.palabakosys.room.entities.EntityJobOrderPayment
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PaymentRepository
@Inject
constructor (
    private val daoPayment: DaoJobOrderPayment,
) {
    suspend fun get(id: UUID): EntityJobOrderPayment? {
        return daoPayment.get(id)
    }

    suspend fun save(payment: EntityJobOrderPayment, jobOrderIds: List<UUID>) {
        daoPayment.save(payment, jobOrderIds)
    }
}