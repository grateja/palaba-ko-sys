package com.csi.palabakosys.room.repository

import androidx.lifecycle.LiveData
import com.csi.palabakosys.model.MachineType
import com.csi.palabakosys.room.dao.DaoMachine
import com.csi.palabakosys.room.entities.EntityMachine
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MachineRepository
@Inject constructor (
    private val daoMachine: DaoMachine,
) : BaseRepository<EntityMachine>(daoMachine) {
    override suspend fun get(id: String?) : EntityMachine? {
        if(id == null) return null

        return daoMachine.get(id)
    }

    suspend fun getAll(machineType: MachineType): List<EntityMachine> {
        return daoMachine.getAll(machineType.toString())
    }

    suspend fun getLastStackOrder(machineType: MachineType?) : Int {
        return daoMachine.getLastStackOrder(machineType.toString())?:1
    }

    suspend fun setWorkerId(machineId: String, workerId: String?) {
        daoMachine.setWorkerId(machineId, workerId)
    }
}