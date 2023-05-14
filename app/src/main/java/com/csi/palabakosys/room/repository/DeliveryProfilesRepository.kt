package com.csi.palabakosys.room.repository

import com.csi.palabakosys.app.joborders.create.delivery.MenuDeliveryProfile
import com.csi.palabakosys.room.dao.DaoDeliveryProfile
import com.csi.palabakosys.room.entities.EntityDeliveryProfile
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeliveryProfilesRepository
@Inject
constructor (
    private val daoDeliveryProfile: DaoDeliveryProfile,
) : BaseRepository<EntityDeliveryProfile>(daoDeliveryProfile) {
    override suspend fun get(id: UUID?) : EntityDeliveryProfile? {
        if(id == null) return null
        return daoDeliveryProfile.get(id)
    }

    suspend fun getAll() : List<MenuDeliveryProfile> {
        return daoDeliveryProfile.getAll()
    }
}