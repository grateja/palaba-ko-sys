package com.csi.palabakosys.room.repository

import com.csi.palabakosys.app.joborders.create.services.MenuServiceItem
import com.csi.palabakosys.room.dao.DaoWashService
import com.csi.palabakosys.room.entities.EntityServiceWash
import javax.inject.Inject

class WashServiceRepository
@Inject
constructor (
    private val daoWashService: DaoWashService,
) : BaseRepository<EntityServiceWash>(daoWashService) {
    override suspend fun get(id: String?) : EntityServiceWash? {
        if(id == null) return null
        return daoWashService.get(id)
    }

    suspend fun getAll() : List<MenuServiceItem> {
        return daoWashService.menuItems()
    }
}