package com.csi.palabakosys.room.repository

import androidx.lifecycle.LiveData
import com.csi.palabakosys.model.EnumMachineType
import com.csi.palabakosys.room.dao.DaoMachine
import com.csi.palabakosys.room.entities.EntityMachine
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MachineRepository
@Inject constructor (
    private val daoMachine: DaoMachine,
) : BaseRepository<EntityMachine>(daoMachine) {
    override suspend fun get(id: UUID?) : EntityMachine? {
        if(id == null) return null
        return daoMachine.get(id)
    }

    suspend fun getAll(machineType: EnumMachineType): List<EntityMachine> {
        return daoMachine.getAll(machineType)
    }

    suspend fun getLastStackOrder(machineType: EnumMachineType) : Int {
        return daoMachine.getLastStackOrder(machineType)?:1
    }

//    suspend fun setWorkerId(machineId: UUID, workerId: UUID?) {
//        daoMachine.setWorkerId(machineId, workerId)
//    }

    fun getAllAsLiveData() : LiveData<List<EntityMachine>> = daoMachine.getAllAsLiveData()

    fun getListAsLiveData() = daoMachine.getListAllAsLiveData()

    fun getMachineLiveData(id: UUID) : LiveData<EntityMachine> = daoMachine.getMachineLiveData(id)
}