package com.csi.palabakosys.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.csi.palabakosys.app.joborders.create.delivery.MenuDeliveryProfile
import com.csi.palabakosys.room.entities.EntityDeliveryProfile
import java.util.*

@Dao
abstract class DaoDeliveryProfile : BaseDao<EntityDeliveryProfile> {
    @Query("SELECT * FROM delivery_profiles WHERE id = :id AND deleted_at IS NULL")
    abstract suspend fun get(id: UUID) : EntityDeliveryProfile?

    @Query("SELECT *, 0 as void FROM delivery_profiles WHERE deleted_at IS NULL")
    abstract suspend fun getAll() : List<MenuDeliveryProfile>
}