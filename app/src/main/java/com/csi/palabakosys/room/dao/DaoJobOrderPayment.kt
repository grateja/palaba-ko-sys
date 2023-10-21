package com.csi.palabakosys.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.csi.palabakosys.app.joborders.payment.JobOrderPaymentMinimal
import com.csi.palabakosys.room.entities.*
import java.time.Instant
import java.time.LocalDate
import java.util.*

@Dao
interface DaoJobOrderPayment {
    @Query("SELECT * FROM job_order_payments WHERE id = :id")
    fun getPaymentWithJobOrders(id: UUID) : LiveData<EntityJobOrderPaymentFull>

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

    @Query("UPDATE job_order_payments SET deleted_at = :deletedAt WHERE id = :paymentId")
    fun unlinkPayment(paymentId: UUID, deletedAt: Instant = Instant.now())

    @Query("UPDATE job_orders SET payment_id = null WHERE payment_id = :paymentId")
    fun unlinkJobOrders(paymentId: UUID)

    @Transaction
    suspend fun deletePayment(paymentId: UUID) {
        unlinkJobOrders(paymentId)
        unlinkPayment(paymentId)
    }

    @Query("SELECT DISTINCT cashless_provider FROM job_order_payments WHERE cashless_provider IS NOT NULL ORDER BY cashless_provider")
    fun getCashlessProviders(): LiveData<List<String>>

    @Query("SELECT jop.* FROM job_order_payments jop JOIN job_orders jo ON jop.id = jo.payment_id WHERE jo.id = :jobOrderId AND jo.deleted_at IS NULL")
    suspend fun getByJobOrderId(jobOrderId: UUID?) : EntityJobOrderPaymentFull?

    @Query("SELECT SUM(amount_due) FROM job_order_payments WHERE payment_method = 1 AND deleted_at IS NULL AND void_date IS NULL AND strftime('%Y-%m-%d', created_at / 1000, 'unixepoch') = :dateFrom OR ( :dateTo IS NOT NULL AND strftime('%Y-%m-%d', created_at / 1000, 'unixepoch') BETWEEN :dateFrom AND :dateTo )")
    fun getCashCollections(dateFrom: LocalDate, dateTo: LocalDate?) : LiveData<Float>

    @Query("SELECT cashless_provider, COUNT(*) as count, SUM(cashless_amount) as amount " +
            "FROM job_order_payments " +
            "WHERE payment_method = 2 " +
            "AND deleted_at IS NULL " +
            "AND void_date IS NULL " +
            "AND (strftime('%Y-%m-%d', created_at / 1000, 'unixepoch') = :dateFrom " +
            "OR (:dateTo IS NOT NULL " +
            "AND strftime('%Y-%m-%d', created_at / 1000, 'unixepoch') BETWEEN :dateFrom AND :dateTo)) " +
            "GROUP BY cashless_provider " +
            "UNION " +
            "SELECT 'Total', COUNT(*), SUM(cashless_amount) " +
            "FROM job_order_payments " +
            "WHERE payment_method = 2 " +
            "AND deleted_at IS NULL " +
            "AND void_date IS NULL " +
            "AND (strftime('%Y-%m-%d', created_at / 1000, 'unixepoch') = :dateFrom " +
            "OR (:dateTo IS NOT NULL " +
            "AND strftime('%Y-%m-%d', created_at / 1000, 'unixepoch') BETWEEN :dateFrom AND :dateTo))")
    fun getCashlessPayments(dateFrom: LocalDate, dateTo: LocalDate?): LiveData<List<EntityCashlessPaymentAggrResult>>
}