package com.csi.palabakosys.room.dao

import androidx.room.*
import com.csi.palabakosys.app.joborders.create.services.MenuServiceItem
import com.csi.palabakosys.room.entities.EntityServiceWash

@Dao
abstract class DaoWashService : BaseDao<EntityServiceWash> {
    @Query("SELECT * FROM wash_services WHERE id = :id")
    abstract suspend fun get(id: String) : EntityServiceWash?

    @Query("SELECT * FROM wash_services")
    abstract suspend fun getAll() : List<EntityServiceWash>

    @Query("DELETE FROM wash_services WHERE id = :id")
    abstract suspend fun delete(id: String)

    @Query("SELECT id, name, minutes, price, machine_type, wash_type, 1 as quantity FROM wash_services")
    abstract suspend fun menuItems() : List<MenuServiceItem>
}