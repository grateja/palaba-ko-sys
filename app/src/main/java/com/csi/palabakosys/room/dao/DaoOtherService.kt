package com.csi.palabakosys.room.dao

import androidx.room.*
import com.csi.palabakosys.room.entities.EntityServiceOther

@Dao
abstract class DaoOtherService : BaseDao<EntityServiceOther> {
    @Query("SELECT * FROM other_services WHERE id = :id")
    abstract suspend fun get(id: String) : EntityServiceOther?

    @Query("SELECT * FROM other_services")
    abstract suspend fun getAll() : List<EntityServiceOther>
}