package com.csi.palabakosys.room.repository

import com.csi.palabakosys.app.joborders.create.services.MenuServiceItem
import com.csi.palabakosys.room.dao.DaoService
import com.csi.palabakosys.room.entities.EntityService
import javax.inject.Inject

class WashServiceRepository
@Inject
constructor (
    private val daoWashService: DaoService,
) : BaseRepository<EntityService>(daoWashService) {
    override suspend fun get(id: String?) : EntityService? {
        if(id == null) return null
        return daoWashService.get(id)
    }

    suspend fun getAll() : List<MenuServiceItem> {
        return daoWashService.menuItems()
    }
}