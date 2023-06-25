package com.csi.palabakosys.room.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.csi.palabakosys.app.customers.CustomerMinimal
import com.csi.palabakosys.app.customers.list.CustomerListItem
import com.csi.palabakosys.app.customers.list.CustomerQueryResult
import com.csi.palabakosys.room.entities.EntityCustomer
import java.util.UUID

@Dao
interface DaoCustomer : BaseDao<EntityCustomer> {
    @Query("SELECT * FROM customers WHERE id = :id AND deleted_at IS NULL")
    abstract suspend fun get(id: UUID) : EntityCustomer?

    @Query("SELECT * FROM customers WHERE name LIKE '%' || :keyword || '%' AND deleted_at IS NULL ORDER BY name LIMIT 10")
    abstract suspend fun getAll(keyword: String) : List<EntityCustomer>

    @Query("SELECT crn FROM customers WHERE deleted_at IS NULL ORDER BY crn DESC")
    abstract suspend fun getLastCRN() : String?

//    @Query("SELECT id, crn, name, address, (SELECT COUNT(*) FROM job_orders WHERE payment_id IS NULL AND customer_id = customers.id AND job_orders.deleted_at IS NULL and job_orders.void_date IS NULL) as unpaid FROM customers WHERE name LIKE '%' || :keyword || '%' OR crn like '%' || :keyword || '%' AND deleted_at IS NULL ORDER BY unpaid DESC, name ASC  LIMIT :itemPerPage OFFSET :offset")
    @Query("SELECT c.id, c.crn, c.name, c.address, COUNT(jo.id) AS unpaid FROM customers c LEFT JOIN job_orders jo ON jo.payment_id IS NULL AND jo.customer_id = c.id AND jo.deleted_at IS NULL AND jo.void_date IS NULL WHERE c.name LIKE '%' || :keyword || '%' OR c.crn LIKE '%' || :keyword || '%' AND c.deleted_at IS NULL GROUP BY c.id, c.crn, c.name, c.address ORDER BY unpaid DESC, c.name ASC LIMIT :itemPerPage OFFSET :offset")
    abstract suspend fun getCustomersMinimal(keyword: String?, itemPerPage: Int, offset: Int): List<CustomerMinimal>

    @Query("SELECT cu.* FROM customers cu WHERE cu.name LIKE '%' || :keyword || '%' OR cu.crn like '%' || :keyword || '%' AND cu.deleted_at IS NULL ORDER BY" +
            " CASE WHEN :orderBy = 'First Visit' AND :sortDirection = 'ASC' THEN cu.created_at END ASC, " +
            " CASE WHEN :orderBy = 'First Visit' AND :sortDirection = 'DESC' THEN cu.created_at END DESC " +
            " LIMIT 20 OFFSET :offset")
    fun load(keyword: String?, orderBy: String?, sortDirection: String?, offset: Int): List<CustomerListItem>

    @Query("SELECT COUNT(*) FROM customers WHERE name LIKE '%' || :keyword || '%' OR crn like '%' || :keyword || '%' AND deleted_at IS NULL")
    fun count(keyword: String?) : Int

    @Transaction
    suspend fun getListItem(keyword: String?, orderBy: String?, sortDirection: String?, offset: Int): CustomerQueryResult {
        return CustomerQueryResult(
            load(keyword, orderBy, sortDirection, offset),
            count(keyword)
        )
    }

    @Query("SELECT EXISTS(SELECT * FROM customers WHERE name LIKE :name AND deleted_at IS NULL)")
    abstract suspend fun checkName(name: String?): Boolean

    @Query("SELECT * FROM customers WHERE crn LIKE :crn AND deleted_at IS NULL LIMIT 1")
    abstract suspend fun getCustomerMinimalByCRN(crn: String?): CustomerMinimal?
}