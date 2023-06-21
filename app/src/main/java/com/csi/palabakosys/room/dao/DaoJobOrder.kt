package com.csi.palabakosys.room.dao

import androidx.room.*
import com.csi.palabakosys.app.joborders.list.JobOrderListItem
import com.csi.palabakosys.app.joborders.list.JobOrderQueryResult
import com.csi.palabakosys.app.joborders.payment.JobOrderPaymentMinimal
import com.csi.palabakosys.room.entities.*
import com.csi.palabakosys.util.toUUID
import java.time.Instant
import java.util.*

@Dao
interface DaoJobOrder {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertJobOrder(entityJobOrder: EntityJobOrder)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertJobOrderService(entityJobOrderService: List<EntityJobOrderService>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertJobOrderProduct(entityJobOrderProduct: List<EntityJobOrderProduct>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertJobOrderExtras(entityJobOrderExtras: List<EntityJobOrderExtras>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDeliveryCharge(entityJobOrderDeliveryCharge: EntityJobOrderDeliveryCharge)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDiscount(entityJobOrderDiscount: EntityJobOrderDiscount)

    @Delete
    fun deleteProducts(products: List<EntityJobOrderProduct>)

    @Delete
    fun deleteServices(services: List<EntityJobOrderService>)

    @Query("UPDATE products SET current_stock = current_stock - COALESCE((SELECT (:newQuantity - quantity) FROM job_order_products WHERE id = :joProductId), :newQuantity) WHERE id = :productId")
    fun updateQuantity(productId: String?, joProductId: String?, newQuantity: Int)

    @Transaction
    suspend fun save(entityJobOrderWithItems: EntityJobOrderWithItems) {
        insertJobOrder(entityJobOrderWithItems.jobOrder)

        entityJobOrderWithItems.services?.let {
            insertJobOrderService(it)
        }
        entityJobOrderWithItems.extras?.let {
            insertJobOrderExtras(it)
        }
        entityJobOrderWithItems.deliveryCharge?.let {
            insertDeliveryCharge(it)
        }
        entityJobOrderWithItems.discount?.let {
            insertDiscount(it)
        }
        entityJobOrderWithItems.products?.let {
            it.forEach { jop ->
                updateQuantity(jop.productId.toString(), jop.id.toString(), jop.quantity)
            }
            insertJobOrderProduct(it)
        }
    }

    @Transaction
    @Query("SELECT * FROM job_orders WHERE id = :id")
    suspend fun getJobOrderWithItems(id: UUID) : EntityJobOrderWithItems?

    @Query("SELECT * FROM job_orders WHERE id = :id")
    suspend fun get(id: UUID?) : EntityJobOrder?

    @Query("DELETE FROM job_orders WHERE id = :id")
    suspend fun delete(id: UUID)

    @Query("SELECT job_order_number FROM job_orders WHERE deleted_at IS NULL AND void_date IS NULL ORDER BY job_order_number DESC")
    suspend fun getLastJobOrderNumber() : String?

//    @Update
//    suspend fun voidJobOrder(jobOrder: EntityJobOrder)

    @Transaction
    @Query("SELECT * FROM job_orders WHERE customer_id = :customerId AND payment_id IS NULL AND DATE(`created_at`/1000, 'unixepoch', 'localtime') = DATE('now', 'localtime') AND deleted_at IS NULL AND void_date IS NULL LIMIT 1")
    suspend fun getCurrentJobOrder(customerId: UUID?): EntityJobOrderWithItems?

    @Query("SELECT * FROM job_orders WHERE customer_id = :customerId AND payment_id IS NULL AND deleted_at IS NULL AND void_date IS NULL")
    suspend fun getAllUnpaidByCustomerId(customerId: UUID?): List<JobOrderPaymentMinimal>

    @Query("SELECT * FROM job_orders WHERE customer_id = :customerId AND payment_id IS NULL AND deleted_at IS NULL AND void_date IS NULL AND (:jobOrderId IS NULL OR (id IS NOT NULL AND id <> :jobOrderId))")
    suspend fun getPreviousUnpaidByCustomerId(customerId: UUID, jobOrderId: UUID?): List<JobOrderPaymentMinimal>

    @Query("SELECT jo.id, jo.job_order_number, jo.discounted_amount, jo.payment_id, jo.customer_id, jo.created_at, cu.name, cu.crn, pa.created_at as date_paid FROM job_orders jo JOIN customers cu ON jo.customer_id = cu.id LEFT JOIN job_order_payments pa ON jo.payment_id = pa.id WHERE cu.name LIKE '%' || :keyword || '%' AND (jo.deleted_at IS NULL AND jo.void_date IS NULL) ORDER BY " +
        " CASE WHEN :orderBy = 'created_at' AND :sortDirection = 'ASC' THEN jo.created_at END ASC, " +
        " CASE WHEN :orderBy = 'date_paid' AND :sortDirection = 'ASC' THEN pa.created_at END ASC, " +
        " CASE WHEN :orderBy = 'customer_name' AND :sortDirection = 'ASC' THEN cu.name END ASC, " +
        " CASE WHEN :orderBy = 'created_at' AND :sortDirection = 'DESC' THEN jo.created_at END DESC, " +
        " CASE WHEN :orderBy = 'date_paid' AND :sortDirection = 'DESC' THEN pa.created_at END DESC, " +
        " CASE WHEN :orderBy = 'customer_name' AND :sortDirection = 'DESC' THEN cu.name END DESC " +
        " LIMIT 20 OFFSET :offset")
    fun load(keyword: String?, orderBy: String?, sortDirection: String?, offset: Int): List<JobOrderListItem>

    @Query("SELECT COUNT(*) FROM job_orders jo JOIN customers cu ON jo.customer_id = cu.id WHERE cu.name LIKE '%' || :keyword || '%'")
    fun count(keyword: String?): Int

    @Transaction
    suspend fun queryResult(keyword: String?, orderBy: String?, sortDirection: String?, offset: Int) : JobOrderQueryResult {
        return JobOrderQueryResult(
            load(keyword, orderBy, sortDirection, offset),
            count(keyword)
        )
    }

    @Query("UPDATE job_orders SET void_by = :userId, void_remarks = :remarks, void_date = :voidDate WHERE id = :jobOrderId")
    suspend fun voidJobOrder(jobOrderId: UUID, userId: UUID?, remarks: String?, voidDate: Instant? = Instant.now())

    @Query("UPDATE job_order_payments SET void_by = :userId, void_remarks = :remarks, void_date = :voidDate WHERE id = :paymentId")
    suspend fun voidPayment(paymentId: UUID?, userId: UUID?, remarks: String?, voidDate: Instant? = Instant.now())

    @Query("UPDATE job_order_services SET deleted_at = :deletedAt WHERE job_order_id = :jobOrderId")
    suspend fun clearServices(jobOrderId: UUID, deletedAt: Instant? = Instant.now())

    @Query("UPDATE job_order_products SET deleted_at = :deletedAt WHERE job_order_id = :jobOrderId")
    suspend fun clearProducts(jobOrderId: UUID, deletedAt: Instant? = Instant.now())

    @Query("UPDATE job_order_extras SET deleted_at = :deletedAt WHERE job_order_id = :jobOrderId")
    suspend fun clearExtras(jobOrderId: UUID, deletedAt: Instant? = Instant.now())

    @Query("UPDATE job_order_delivery_charges SET deleted_at = :deletedAt WHERE id = :jobOrderId")
    suspend fun clearDeliveryCharges(jobOrderId: UUID, deletedAt: Instant? = Instant.now())

    @Query("UPDATE job_order_discounts SET deleted_at = :deletedAt WHERE id = :jobOrderId")
    suspend fun clearDiscounts(jobOrderId: UUID, deletedAt: Instant? = Instant.now())

    @Query("UPDATE products SET current_stock = (current_stock + :quantity) WHERE id = :productId")
    suspend fun returnProduct(productId: UUID, quantity: Int)


    @Transaction
    suspend fun cancelJobOrder(jobOrderWithItems: EntityJobOrderWithItems, jobOrderVoid: EntityJobOrderVoid) {
        val jobOrderId = jobOrderWithItems.jobOrder.id
        val paymentId = jobOrderWithItems.payment?.id

        voidJobOrder(jobOrderId, jobOrderVoid.voidByUserId, jobOrderVoid.remarks)
        voidPayment(paymentId, jobOrderVoid.voidByUserId, jobOrderVoid.remarks)
        jobOrderWithItems.products?.onEach {
            returnProduct(it.productId, it.quantity)
        }
        clearServices(jobOrderId)
        clearProducts(jobOrderId)
        clearExtras(jobOrderId)
        clearDeliveryCharges(jobOrderId)
        clearDiscounts(jobOrderId)
    }
}