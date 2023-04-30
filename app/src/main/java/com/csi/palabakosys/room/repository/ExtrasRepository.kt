package com.csi.palabakosys.room.repository

import com.csi.palabakosys.app.joborders.create.extras.MenuExtrasItem
import com.csi.palabakosys.room.dao.DaoExtras
import com.csi.palabakosys.room.dao.DaoOtherService
import com.csi.palabakosys.room.entities.EntityExtras
import com.csi.palabakosys.room.entities.EntityServiceOther
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExtrasRepository
@Inject
constructor (
    private val daoExtras: DaoExtras,
) : BaseRepository<EntityExtras>(daoExtras) {
    override suspend fun get(id: String?) : EntityExtras? {
        if(id == null) return null
        return daoExtras.get(id)
    }

    suspend fun getAll(keyword: String) : List<MenuExtrasItem> {
        return daoExtras.getAll(keyword)
    }
}