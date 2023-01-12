package com.csi.palabakosys.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.csi.palabakosys.room.entities.EntityShop

@Dao
abstract class DaoShop : BaseDao<EntityShop> {
    @Query("SELECT * FROM shops LIMIT 1")
    abstract suspend fun get() : EntityShop?
}