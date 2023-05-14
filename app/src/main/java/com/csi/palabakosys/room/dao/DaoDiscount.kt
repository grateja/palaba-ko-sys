package com.csi.palabakosys.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.csi.palabakosys.app.joborders.create.discount.MenuDiscount
import com.csi.palabakosys.room.entities.EntityDiscount
import java.util.UUID

@Dao
abstract class DaoDiscount : BaseDao<EntityDiscount> {
    @Query("SELECT * FROM discounts WHERE id = :id")
    abstract suspend fun get(id: UUID) : EntityDiscount?

    @Query("SELECT * FROM discounts WHERE name LIKE '%' || :keyword || '%' ORDER BY name")
    abstract suspend fun getAll(keyword: String) : List<MenuDiscount>
}