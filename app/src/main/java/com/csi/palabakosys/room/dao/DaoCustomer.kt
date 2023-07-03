package com.csi.palabakosys.room.dao

import androidx.lifecycle.LiveData
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
    suspend fun get(id: UUID) : EntityCustomer?

    @Query("SELECT * FROM customers WHERE name LIKE '%' || :keyword || '%' AND deleted_at IS NULL ORDER BY name LIMIT 10")
    suspend fun getAll(keyword: String) : List<EntityCustomer>

    @Query("SELECT crn FROM customers WHERE deleted_at IS NULL ORDER BY crn DESC")
    suspend fun getLastCRN() : String?

//    @Query("SELECT id, crn, name, address, (SELECT COUNT(*) FROM job_orders WHERE payment_id IS NULL AND customer_id = customers.id AND job_orders.deleted_at IS NULL and job_orders.void_date IS NULL) as unpaid FROM customers WHERE name LIKE '%' || :keyword || '%' OR crn like '%' || :keyword || '%' AND deleted_at IS NULL ORDER BY unpaid DESC, name ASC  LIMIT :itemPerPage OFFSET :offset")
    @Query("SELECT c.id, c.crn, c.name, c.address, COUNT(jo.id) AS unpaid FROM customers c LEFT JOIN job_orders jo ON jo.payment_id IS NULL AND jo.customer_id = c.id AND jo.deleted_at IS NULL AND jo.void_date IS NULL WHERE c.name LIKE '%' || :keyword || '%' OR c.crn LIKE '%' || :keyword || '%' AND c.deleted_at IS NULL GROUP BY c.id, c.crn, c.name, c.address ORDER BY unpaid DESC, c.name ASC LIMIT :itemPerPage OFFSET :offset")
    suspend fun getCustomersMinimal(keyword: String?, itemPerPage: Int, offset: Int): List<CustomerMinimal>

    @Query("SELECT cu.*, " +
            "SUM(CASE WHEN jo.payment_id IS NOT NULL THEN 1 ELSE 0 END) AS paid_count, " +
            "COUNT(jo.id) as total_jo, MIN(jo.created_at) AS first_visit, MAX(jo.created_at) AS last_visit FROM customers cu LEFT JOIN job_orders jo ON jo.customer_id = cu.id WHERE " +
            "(:hideAllWithoutJO = 1 AND cu.id IN (SELECT DISTINCT customer_id FROM job_orders)) OR " +
            "(:hideAllWithoutJO = 0) AND (cu.name LIKE '%' || :keyword || '%' OR cu.crn LIKE '%' || :keyword || '%') AND cu.deleted_at IS NULL " +
            "GROUP BY cu.id " +
            "ORDER BY " +
            "CASE WHEN :orderBy = 'Name' AND :sortDirection = 'ASC' THEN cu.name END ASC, " +
            "CASE WHEN :orderBy = 'Name' AND :sortDirection = 'DESC' THEN cu.name END DESC, " +
            "CASE WHEN :orderBy = 'First Visit' AND :sortDirection = 'ASC' THEN first_visit END ASC, " +
            "CASE WHEN :orderBy = 'First Visit' AND :sortDirection = 'DESC' THEN first_visit END DESC, " +
            "CASE WHEN :orderBy = 'Last Visit' AND :sortDirection = 'ASC' THEN last_visit END ASC, " +
            "CASE WHEN :orderBy = 'Last Visit' AND :sortDirection = 'DESC' THEN last_visit END DESC, " +
            "CASE WHEN :orderBy = 'Number of Job Orders' AND :sortDirection = 'ASC' THEN total_jo END ASC, " +
            "CASE WHEN :orderBy = 'Number of Job Orders' AND :sortDirection = 'DESC' THEN total_jo END DESC " +
            "LIMIT 20 OFFSET :offset")
    fun load(keyword: String?, orderBy: String?, sortDirection: String?, offset: Int, hideAllWithoutJO: Boolean): List<CustomerListItem>

    @Query("SELECT COUNT(*) FROM customers WHERE name LIKE '%' || :keyword || '%' OR crn like '%' || :keyword || '%' AND deleted_at IS NULL")
    fun count(keyword: String?) : Int

    @Transaction
    suspend fun getListItem(keyword: String?, orderBy: String?, sortDirection: String?, offset: Int, hideAllWithoutJO: Boolean): CustomerQueryResult {
        return CustomerQueryResult(
            load(keyword, orderBy, sortDirection, offset, hideAllWithoutJO),
            count(keyword)
        )
    }

    @Query("SELECT EXISTS(SELECT * FROM customers WHERE name LIKE :name AND deleted_at IS NULL)")
    suspend fun checkName(name: String?): Boolean

    @Query("SELECT * FROM customers WHERE crn LIKE :crn AND deleted_at IS NULL LIMIT 1")
    suspend fun getCustomerMinimalByCRN(crn: String?): CustomerMinimal?
}