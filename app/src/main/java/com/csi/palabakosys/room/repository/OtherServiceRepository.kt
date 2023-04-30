package com.csi.palabakosys.room.repository

import com.csi.palabakosys.room.dao.DaoOtherService
import com.csi.palabakosys.room.entities.EntityServiceOther
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OtherServiceRepository
@Inject
constructor (
    private val daoOtherService: DaoOtherService,
) : BaseRepository<EntityServiceOther>(daoOtherService) {
    override suspend fun get(id: String?) : EntityServiceOther? {
        if(id == null) return null
        return daoOtherService.get(id)
    }

    suspend fun getAll() : List<EntityServiceOther> {
        return daoOtherService.getAll()
    }
}