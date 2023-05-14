package com.csi.palabakosys.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.csi.palabakosys.room.entities.EntityInventoryLog
import java.util.UUID

@Dao
abstract class DaoInventoryLog : BaseDao<EntityInventoryLog> {
    @Query("SELECT * FROM inventory_log WHERE id = :id")
    abstract suspend fun get(id: UUID) : EntityInventoryLog?

    @Query("SELECT * FROM inventory_log WHERE product_name LIKE '%' || :keyword || '%' ORDER BY product_name")
    abstract suspend fun getAll(keyword: String) : List<EntityInventoryLog>
}