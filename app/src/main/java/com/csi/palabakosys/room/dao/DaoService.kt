package com.csi.palabakosys.room.dao

import androidx.room.*
import com.csi.palabakosys.app.joborders.create.services.MenuServiceItem
import com.csi.palabakosys.room.entities.EntityService

@Dao
abstract class DaoService : BaseDao<EntityService> {
    @Query("SELECT * FROM services WHERE id = :id")
    abstract suspend fun get(id: String) : EntityService?

    @Query("SELECT * FROM services")
    abstract suspend fun getAll() : List<EntityService>

    @Query("DELETE FROM services WHERE id = :id")
    abstract suspend fun delete(id: String)

    @Query("SELECT id, id as service_id, name, svc_minutes, price, svc_machine_type, svc_wash_type, 1 as quantity FROM services")
    abstract suspend fun menuItems() : List<MenuServiceItem>
}