package com.csi.palabakosys.room.dao

import androidx.room.*
import com.csi.palabakosys.app.joborders.create.products.MenuProductItem
import com.csi.palabakosys.room.entities.EntityProduct
import java.util.UUID

@Dao
abstract class DaoProduct : BaseDao<EntityProduct> {
    @Query("SELECT * FROM products WHERE id = :id")
    abstract suspend fun get(id: UUID) : EntityProduct?

    @Query("SELECT * FROM products")
    abstract suspend fun getAll() : List<EntityProduct>

    @Query("SELECT id, name, price, measure_unit, unit_per_serve, 1 as quantity, current_stock, product_type FROM products")
    abstract suspend fun menuItems(): List<MenuProductItem>
}