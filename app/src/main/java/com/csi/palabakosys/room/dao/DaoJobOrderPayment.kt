package com.csi.palabakosys.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.csi.palabakosys.app.joborders.payment.JobOrderPaymentMinimal
import com.csi.palabakosys.room.entities.EntityJobOrderPayment
import com.csi.palabakosys.room.entities.EntityJobOrderPaymentFull
import java.util.*

@Dao
interface DaoJobOrderPayment {
    @Query("SELECT * FROM job_order_payments WHERE id = :id")
    suspend fun getPaymentWithJobOrders(id: UUID) : EntityJobOrderPaymentFull?

    @Query("SELECT * FROM job_order_payments WHERE id = :id")
    suspend fun get(id: UUID?): EntityJobOrderPayment?

    @Query("UPDATE job_orders SET payment_id = :paymentId WHERE id IN (:jobOrderIds)")
    fun setJobOrderPayment(paymentId: UUID, jobOrderIds: List<UUID>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPayment(payment: EntityJobOrderPayment)

    @Transaction
    suspend fun save(
        entityJobOrderPayment: EntityJobOrderPayment,
        jobOrderIds: List<UUID>
    ) : EntityJobOrderPayment {
        insertPayment(entityJobOrderPayment)
        setJobOrderPayment(entityJobOrderPayment.id, jobOrderIds)
        return entityJobOrderPayment
    }

    @Query("SELECT DISTINCT cashless_provider FROM job_order_payments WHERE cashless_provider IS NOT NULL ORDER BY cashless_provider")
    fun getCashlessProviders(): LiveData<List<String>>

    @Query("SELECT jop.* FROM job_order_payments jop JOIN job_orders jo ON jop.id = jo.payment_id WHERE jo.id = :jobOrderId AND jo.deleted_at IS NULL")
    suspend fun getByJobOrderId(jobOrderId: UUID?) : EntityJobOrderPayment?
}