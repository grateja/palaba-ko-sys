package com.csi.palabakosys.room.repository

import com.csi.palabakosys.room.dao.DaoMachineRemarks
import com.csi.palabakosys.room.entities.EntityMachineRemarks
import javax.inject.Inject

class MachineRemarksRepository
@Inject
constructor (
    private val dao: DaoMachineRemarks,
) : BaseRepository<EntityMachineRemarks>(dao) {
    override suspend fun get(id: String?) : EntityMachineRemarks? {
        if(id == null) return null
        return dao.get(id)
    }
}