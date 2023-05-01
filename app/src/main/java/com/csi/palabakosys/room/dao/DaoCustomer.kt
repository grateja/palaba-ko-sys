package com.csi.palabakosys.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.csi.palabakosys.app.customers.CustomerMinimal
import com.csi.palabakosys.room.entities.EntityCustomer

@Dao
abstract class DaoCustomer : BaseDao<EntityCustomer> {
    @Query("SELECT * FROM customers WHERE id = :id")
    abstract suspend fun get(id: String) : EntityCustomer?

    @Query("SELECT * FROM customers WHERE name LIKE '%' || :keyword || '%' ORDER BY name")
    abstract suspend fun getAll(keyword: String) : List<EntityCustomer>

    @Query("SELECT crn FROM customers ORDER BY crn DESC")
    abstract suspend fun getLastCRN() : String?

    @Query("SELECT id, crn, name, address FROM customers WHERE name LIKE '%' || :keyword || '%' OR crn like '%' || :keyword || '%' ORDER BY name")
    abstract suspend fun getCustomersMinimal(keyword: String?): List<CustomerMinimal>

    @Query("SELECT EXISTS(SELECT * FROM customers WHERE name LIKE :name)")
    abstract suspend fun checkName(name: String?): Boolean
}