package com.csi.palabakosys.room.dao

import androidx.room.*
import com.csi.palabakosys.room.entities.EntityServiceWash

@Dao
abstract class DaoWashService : BaseDao<EntityServiceWash> {
    @Query("SELECT * FROM wash_services WHERE id = :id")
    abstract suspend fun get(id: String) : EntityServiceWash?

    @Query("SELECT * FROM wash_services")
    abstract suspend fun getAll() : List<EntityServiceWash>

    @Query("DELETE FROM wash_services WHERE id = :id")
    abstract suspend fun delete(id: String)
}