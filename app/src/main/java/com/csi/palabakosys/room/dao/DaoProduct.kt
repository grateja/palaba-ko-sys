package com.csi.palabakosys.room.dao

import androidx.room.*
import com.csi.palabakosys.room.entities.EntityProduct

@Dao
abstract class DaoProduct : BaseDao<EntityProduct> {
    @Query("SELECT * FROM products WHERE id = :id")
    abstract suspend fun get(id: String) : EntityProduct?

    @Query("SELECT * FROM products")
    abstract suspend fun getAll() : List<EntityProduct>
}