package com.csi.palabakosys.room.repository

import androidx.lifecycle.LiveData
import com.csi.palabakosys.app.dashboard.data.DateFilter
import com.csi.palabakosys.app.remote.panel.MachineTile
import com.csi.palabakosys.model.EnumMachineType
import com.csi.palabakosys.room.dao.DaoMachine
import com.csi.palabakosys.room.entities.EntityMachine
import com.csi.palabakosys.room.entities.EntityMachineUsageDetails
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

    fun getAllAsLiveData() = daoMachine.getAllAsLiveData()

    fun getListAsLiveData(machineType: EnumMachineType) = daoMachine.getListAllAsLiveData(machineType)

    fun getMachineLiveData(id: UUID?) = daoMachine.getMachineLiveData(id)

    fun getDashboard(dateFilter: DateFilter) = daoMachine.getDashboard(dateFilter.dateFrom, dateFilter.dateTo)

    suspend fun getMachineUsage(machineId: UUID, keyword: String?, page: Int, dateFilter: DateFilter?): List<EntityMachineUsageDetails> {
        val offset = (20 * page) - 20
        return daoMachine.getMachineUsage(machineId, keyword, offset, dateFilter?.dateFrom, dateFilter?.dateTo)
    }
}