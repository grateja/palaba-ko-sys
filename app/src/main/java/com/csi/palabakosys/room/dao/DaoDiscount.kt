package com.csi.palabakosys.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.csi.palabakosys.room.entities.EntityDiscount

@Dao
abstract class DaoDiscount : BaseDao<EntityDiscount> {
    @Query("SELECT * FROM discounts WHERE id = :id")
    abstract suspend fun get(id: String) : EntityDiscount?

    @Query("SELECT * FROM discounts WHERE name LIKE '%' || :keyword || '%' ORDER BY name")
    abstract suspend fun getAll(keyword: String) : List<EntityDiscount>
}