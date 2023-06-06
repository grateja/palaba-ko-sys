package com.csi.palabakosys.room.dao

import androidx.room.*
import com.csi.palabakosys.app.joborders.create.services.MenuServiceItem
import com.csi.palabakosys.room.entities.EntityService
import java.util.UUID

@Dao
abstract class DaoService : BaseDao<EntityService> {
    @Query("SELECT * FROM services WHERE id = :id")
    abstract suspend fun get(id: UUID) : EntityService?

    @Query("SELECT * FROM services")
    abstract suspend fun getAll() : List<EntityService>

    @Query("SELECT *, 1 as quantity, 0 as used FROM services WHERE deleted_at IS NULL")
    abstract suspend fun menuItems() : List<MenuServiceItem>
}