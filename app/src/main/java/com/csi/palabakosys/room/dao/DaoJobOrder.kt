package com.csi.palabakosys.room.dao

import androidx.room.*
import com.csi.palabakosys.app.joborders.payment.JobOrderPaymentMinimal
import com.csi.palabakosys.room.entities.*
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

//    @Query("UPDATE products SET current_stock = current_stock - :quantity WHERE id = :id")
//    fun reduceProducts(quantity: Int, id: String)

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
//            deleteProducts(it.filter { p -> p.deletedAt != null })
        }

//        entityJobOrderWithItems.products?.forEach {
//            reduceProducts(it.quantity, it.productId.toString())
//        }
    }

    @Transaction
    @Query("SELECT * FROM job_orders WHERE id = :id")
    suspend fun get(id: UUID) : EntityJobOrderWithItems?

    @Query("DELETE FROM job_orders WHERE id = :id")
    suspend fun delete(id: UUID)

//    @Query("SELECT *, " +
//            "(IFNULL((SELECT SUM(price) FROM job_order_services WHERE job_order_id = job_orders.id), 0) + " +
//            "IFNULL((SELECT SUM(price) FROM job_order_products WHERE job_order_id = job_orders.id), 0)" +
//            ") as total_amount," +
//            "(SELECT COUNT(id) FROM job_order_services WHERE job_order_id = job_orders.id) as services_count" +
//            " FROM job_orders WHERE customer_name LIKE '%' || :keyword || '%'" +
//            " ORDER BY created_at DESC")
//    @Query("SELECT job_orders.id, customer_name FROM job_orders JOIN job_order_services ON job_order_services.job_order_id = job_orders.id JOIN job_order_products ON job_order_products.job_order_id = job_orders.id WHERE job_order_number LIKE :keyword || '%' GROUP BY job_orders.id")
//    @Query("SELECT jo.*, jop.created_at as date_paid, jop.balance," +
//            " (" +
//            "  IFNULL((SELECT SUM(price * quantity) FROM job_order_services WHERE job_order_id = jo.id), 0) + " +
//            "  IFNULL((SELECT SUM(price * quantity) FROM job_order_products WHERE job_order_id = jo.id), 0)" +
//            " ) as total_amount, " +
//            " (SELECT COUNT(id) FROM job_order_services WHERE job_order_id = jo.id) as services_count" +
//            "  FROM job_orders jo" +
//            "  LEFT JOIN job_order_payments jop ON job_order_id = jo.id" +
//            "  WHERE (jo.void_date IS NULL OR (jo.void_date IS NOT NULL = :includeVoid)) AND customer_name LIKE '%' || :keyword || '%'" +
//            "  GROUP BY jo.id" +
//            "  ORDER BY created_at DESC")
//    suspend fun getAllWithTotalAmount(keyword: String, includeVoid: Boolean) : List<EntityJobOrderListItem>

    @Query("SELECT job_order_number FROM job_orders ORDER BY job_order_number DESC")
    suspend fun getLastJobOrderNumber() : String?

    @Update
    suspend fun voidJobOrder(jobOrder: EntityJobOrder)

    @Transaction
//    @Query("SELECT jo.*, jop.balance FROM job_orders jo LEFT JOIN job_order_payments jop ON jop.job_order_id = jo.id WHERE DATE(jo.created_at/1000, 'unixepoch', 'localtime') = DATE('now', 'localtime') AND jo.customer_id = :customerId AND jo.deleted_at IS NULL AND void_date IS NULL LIMIT 1")
    @Query("SELECT * FROM job_orders WHERE customer_id = :customerId AND payment_id IS NULL AND DATE(`created_at`/1000, 'unixepoch', 'localtime') = DATE('now', 'localtime') AND deleted_at IS NULL AND void_date IS NULL LIMIT 1")
    suspend fun getCurrentJobOrder(customerId: UUID?): EntityJobOrderWithItems?

    @Query("SELECT * FROM job_orders WHERE customer_id = :customerId AND payment_id IS NULL AND deleted_at IS NULL")
    suspend fun getAllUnpaidByCustomerId(customerId: UUID): List<JobOrderPaymentMinimal>

    @Query("SELECT * FROM job_orders WHERE customer_id = :customerId AND payment_id IS NULL AND deleted_at IS NULL AND DATE(`created_at`/1000, 'unixepoch', 'localtime') > DATE('now', 'localtime')")
    suspend fun getPreviousUnpaidByCustomerId(customerId: UUID): List<JobOrderPaymentMinimal>
}