package com.csi.palabakosys.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.csi.palabakosys.app.joborders.create.extras.MenuExtrasItem
import com.csi.palabakosys.room.entities.EntityExtras

@Dao
abstract class DaoExtras : BaseDao<EntityExtras> {
    @Query("SELECT * FROM extras WHERE id = :id")
    abstract suspend fun get(id: String?) : EntityExtras?

    @Query("SELECT *, 1 as quantity FROM extras WHERE name LIKE '%' || :keyword || '%' ORDER BY name")
    abstract suspend fun getAll(keyword: String) : List<MenuExtrasItem>
}