package com.csi.palabakosys.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.csi.palabakosys.app.extras.ExtrasItemFull
import com.csi.palabakosys.app.joborders.create.extras.MenuExtrasItem
import com.csi.palabakosys.room.entities.EntityExtras
import java.util.UUID

@Dao
abstract class DaoExtras : BaseDao<EntityExtras> {
    @Query("SELECT * FROM extras WHERE id = :id AND deleted_at IS NULL")
    abstract suspend fun get(id: UUID) : EntityExtras?

    @Query("SELECT *, 1 as quantity FROM extras WHERE name LIKE '%' || :keyword || '%' AND deleted_at IS NULL ORDER BY name")
    abstract suspend fun getAll(keyword: String) : List<MenuExtrasItem>

    @Query("SELECT *, 1 as quantity FROM extras WHERE name LIKE '%' || :keyword || '%' AND deleted_at IS NULL ORDER BY name")
    abstract suspend fun filter(keyword: String) : List<ExtrasItemFull>

    @Query("SELECT DISTINCT category FROM extras WHERE category IS NOT NULL ORDER BY category")
    abstract fun getCategories(): LiveData<List<String>>
}