package com.csi.palabakosys.room.dao

import androidx.room.*
import com.csi.palabakosys.room.entities.EntityJobOrderPayment
import java.util.*

@Dao
interface DaoJobOrderPayment {
    @Query("SELECT * FROM job_order_payments WHERE id = :id")
    suspend fun get(id: String) : EntityJobOrderPayment?

    @Query("UPDATE customers SET cash_back = cash_back + :cashBack WHERE id = :customerId")
    fun saveCashBack(customerId: UUID?, cashBack: Float)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(entityJobOrderPayment: EntityJobOrderPayment)

    @Transaction
    suspend fun save(payment: EntityJobOrderPayment, customerId: UUID?, points: Float) {
        save(payment)
        saveCashBack(customerId, points)
    }
}