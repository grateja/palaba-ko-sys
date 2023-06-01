package com.csi.palabakosys.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.csi.palabakosys.app.customers.CustomerMinimal
import com.csi.palabakosys.room.entities.EntityCustomer
import java.util.UUID

@Dao
abstract class DaoCustomer : BaseDao<EntityCustomer> {
    @Query("SELECT * FROM customers WHERE id = :id AND deleted_at IS NULL")
    abstract suspend fun get(id: UUID) : EntityCustomer?

    @Query("SELECT * FROM customers WHERE name LIKE '%' || :keyword || '%' AND deleted_at IS NULL ORDER BY name LIMIT 10")
    abstract suspend fun getAll(keyword: String) : List<EntityCustomer>

    @Query("SELECT crn FROM customers WHERE deleted_at IS NULL ORDER BY crn DESC")
    abstract suspend fun getLastCRN() : String?

    @Query("SELECT id, crn, name, address, (SELECT COUNT(*) FROM job_orders WHERE payment_id IS NULL AND customer_id = customers.id AND job_orders.deleted_at IS NULL and job_orders.void_date IS NULL) as unpaid FROM customers WHERE name LIKE '%' || :keyword || '%' OR crn like '%' || :keyword || '%' AND deleted_at IS NULL ORDER BY unpaid DESC, name ASC LIMIT 15")
    abstract suspend fun getCustomersMinimal(keyword: String?): List<CustomerMinimal>

    @Query("SELECT EXISTS(SELECT * FROM customers WHERE name LIKE :name AND deleted_at IS NULL)")
    abstract suspend fun checkName(name: String?): Boolean

    @Query("SELECT * FROM customers WHERE crn LIKE :crn AND deleted_at IS NULL LIMIT 1")
    abstract suspend fun getCustomerMinimalByCRN(crn: String?): CustomerMinimal?
}