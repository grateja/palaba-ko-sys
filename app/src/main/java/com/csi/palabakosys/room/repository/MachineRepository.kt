package com.csi.palabakosys.room.repository

import com.csi.palabakosys.model.MachineType
import com.csi.palabakosys.room.dao.DaoMachine
import com.csi.palabakosys.room.entities.EntityMachine
import javax.inject.Inject

class MachineRepository
@Inject constructor (
    private val daoMachine: DaoMachine,
) : BaseRepository<EntityMachine>(daoMachine) {
    override suspend fun get(id: String?) : EntityMachine? {
        if(id == null) return null

        return daoMachine.get(id)
    }

    suspend fun getAll() : List<EntityMachine> {
        return daoMachine.getAll()
    }

    suspend fun getLastStackOrder(machineType: MachineType?) : Int {
        return daoMachine.getLastStackOrder(machineType.toString())?:1
    }

    suspend fun setWorkerId(machine: EntityMachine, workerId: String) {
        daoMachine.setWorkerId(machine.id.toString(), workerId)
    }
}