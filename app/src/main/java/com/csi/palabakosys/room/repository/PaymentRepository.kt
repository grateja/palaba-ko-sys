package com.csi.palabakosys.room.repository

import com.csi.palabakosys.room.dao.DaoJobOrderPayment
import com.csi.palabakosys.room.entities.EntityJobOrderPayment
import java.util.*
import javax.inject.Inject

class PaymentRepository
@Inject
constructor (
    private val daoPayment: DaoJobOrderPayment,
) {
    suspend fun get(id: String?): EntityJobOrderPayment? {
        if(id == null) return null
        return daoPayment.get(id)
    }

    suspend fun save(payment: EntityJobOrderPayment, customerId: UUID?, cashBack: Float) {
        daoPayment.save(payment, customerId, cashBack)
    }
}