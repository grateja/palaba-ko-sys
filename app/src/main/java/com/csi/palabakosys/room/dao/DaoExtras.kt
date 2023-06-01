package com.csi.palabakosys.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.csi.palabakosys.app.joborders.create.extras.MenuExtrasItem
import com.csi.palabakosys.room.entities.EntityExtras
import java.util.UUID

@Dao
abstract class DaoExtras : BaseDao<EntityExtras> {
    @Query("SELECT * FROM extras WHERE id = :id AND deleted_at IS NULL")
    abstract suspend fun get(id: UUID) : EntityExtras?

    @Query("SELECT *, 1 as quantity FROM extras WHERE name LIKE '%' || :keyword || '%' AND deleted_at IS NULL ORDER BY name")
    abstract suspend fun getAll(keyword: String) : List<MenuExtrasItem>
}