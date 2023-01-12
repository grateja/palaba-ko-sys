package com.csi.palabakosys.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.csi.palabakosys.room.entities.EntityMachineUsage

@Dao
abstract class DaoMachineUsage : BaseDao<EntityMachineUsage> {
    @Query("SELECT * FROM machine_usages WHERE id = :id")
    abstract suspend fun get(id: String) : EntityMachineUsage?

    @Query("SELECT * FROM machine_usages WHERE customer_name LIKE '%' || :keyword || '%' ORDER BY created_at")
    abstract suspend fun getAll(keyword: String) : List<EntityMachineUsage>
}