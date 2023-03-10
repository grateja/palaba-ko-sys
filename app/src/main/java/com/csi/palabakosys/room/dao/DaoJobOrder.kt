package com.csi.palabakosys.room.dao

import androidx.room.*
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

    @Query("UPDATE products SET current_stock = current_stock - :quantity WHERE id = :id")
    fun reduceProducts(quantity: Int, id: String)

    @Delete
    fun deleteProducts(products: List<EntityJobOrderProduct>)

    @Delete
    fun deleteServices(services: List<EntityJobOrderService>)

    @Transaction
    suspend fun save(entityJobOrderWithItems: EntityJobOrderWithItems) {
        insertJobOrder(entityJobOrderWithItems.jobOrder)

        entityJobOrderWithItems.services?.let {
            insertJobOrderService(it)
        }
        entityJobOrderWithItems.products?.let {
            insertJobOrderProduct(it)
        }

        entityJobOrderWithItems.products?.forEach {
            reduceProducts(it.quantity, it.productId.toString())
        }

//        deleteProducts(entityJobOrderWithItems.products.filter{ it.deletedAt != null })
//        deleteServices(entityJobOrderWithItems.services.filter{ it.deletedAt != null })
    }

    @Transaction
    @Query("SELECT *, 0 as total_amount FROM job_orders WHERE id = :id")
    suspend fun get(id: String) : EntityJobOrderWithItems?

    @Query("DELETE FROM job_orders WHERE id = :id")
    suspend fun delete(id: String)

//    @Query("SELECT *, " +
//            "(IFNULL((SELECT SUM(price) FROM job_order_services WHERE job_order_id = job_orders.id), 0) + " +
//            "IFNULL((SELECT SUM(price) FROM job_order_products WHERE job_order_id = job_orders.id), 0)" +
//            ") as total_amount," +
//            "(SELECT COUNT(id) FROM job_order_services WHERE job_order_id = job_orders.id) as services_count" +
//            " FROM job_orders WHERE customer_name LIKE '%' || :keyword || '%'" +
//            " ORDER BY created_at DESC")
//    @Query("SELECT job_orders.id, customer_name FROM job_orders JOIN job_order_services ON job_order_services.job_order_id = job_orders.id JOIN job_order_products ON job_order_products.job_order_id = job_orders.id WHERE job_order_number LIKE :keyword || '%' GROUP BY job_orders.id")
    @Query("SELECT jo.*, jop.created_at as date_paid, jop.balance," +
            " (" +
            "  IFNULL((SELECT SUM(price * quantity) FROM job_order_services WHERE job_order_id = jo.id), 0) + " +
            "  IFNULL((SELECT SUM(price * quantity) FROM job_order_products WHERE job_order_id = jo.id), 0)" +
            " ) as total_amount, " +
            " (SELECT COUNT(id) FROM job_order_services WHERE job_order_id = jo.id) as services_count" +
            "  FROM job_orders jo" +
            "  LEFT JOIN job_order_payments jop ON job_order_id = jo.id" +
            "  WHERE (jo.void_date IS NULL OR (jo.void_date IS NOT NULL = :includeVoid)) AND customer_name LIKE '%' || :keyword || '%'" +
            "  GROUP BY jo.id" +
            "  ORDER BY created_at DESC")
    suspend fun getAllWithTotalAmount(keyword: String, includeVoid: Boolean) : List<EntityJobOrderListItem>

    @Query("SELECT job_order_number FROM job_orders ORDER BY job_order_number DESC")
    suspend fun getLastJobOrderNumber() : String?

    @Update
    suspend fun voidJobOrder(jobOrder: EntityJobOrder)

    @Transaction
    @Query("SELECT jo.*, jop.balance FROM job_orders jo LEFT JOIN job_order_payments jop ON jop.job_order_id = jo.id WHERE jo.customer_id = :customerId AND jo.deleted_at IS NULL AND void_date IS NULL LIMIT 1")
    suspend fun getCurrentJobOrder(customerId: UUID?): EntityJobOrderWithItems?
}