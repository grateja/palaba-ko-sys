package com.csi.palabakosys.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.csi.palabakosys.app.joborders.create.products.MenuProductItem
import com.csi.palabakosys.app.products.ProductItemFull
import com.csi.palabakosys.room.entities.EntityProduct
import java.util.UUID

@Dao
abstract class DaoProduct : BaseDao<EntityProduct> {
    @Query("SELECT * FROM products WHERE id = :id")
    abstract suspend fun get(id: UUID) : EntityProduct?

    @Query("SELECT * FROM products")
    abstract suspend fun getAll() : List<EntityProduct>

    @Query("SELECT *, 1 as quantity FROM products WHERE deleted_at IS NULL")
    abstract suspend fun menuItems(): List<MenuProductItem>

    @Query("SELECT * FROM products WHERE name LIKE '%' || :keyword || '%' AND deleted_at IS NULL ORDER BY name")
    abstract suspend fun filter(keyword: String): List<ProductItemFull>
}