package com.csi.palabakosys.room.dao

import androidx.room.*
import com.csi.palabakosys.room.entities.EntityServiceDry

@Dao
abstract class DaoDryService : BaseDao<EntityServiceDry> {
    @Query("SELECT * FROM dry_services WHERE id = :id")
    abstract suspend fun get(id: String) : EntityServiceDry?

    @Query("SELECT * FROM dry_services")
    abstract suspend fun getAll() : List<EntityServiceDry>
}