package com.csi.palabakosys.room.repository

import com.csi.palabakosys.room.dao.DaoMachineRemarks
import com.csi.palabakosys.room.entities.EntityMachineRemarks
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MachineRemarksRepository
@Inject
constructor (
    private val dao: DaoMachineRemarks,
) : BaseRepository<EntityMachineRemarks>(dao) {
    override suspend fun get(id: UUID?) : EntityMachineRemarks? {
        if(id == null) return null
        return dao.get(id)
    }
}