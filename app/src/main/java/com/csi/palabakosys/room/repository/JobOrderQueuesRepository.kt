package com.csi.palabakosys.room.repository

import com.csi.palabakosys.model.MachineType
import com.csi.palabakosys.room.dao.DaoJobOrderQueues
import com.csi.palabakosys.room.entities.EntityAvailableService
import com.csi.palabakosys.room.entities.EntityCustomerQueueService
import java.lang.Exception
import javax.inject.Inject

class JobOrderQueuesRepository
@Inject
constructor (
    private val dao: DaoJobOrderQueues,
) {
    suspend fun getByMachineType(machineType: MachineType?) : List<EntityCustomerQueueService> {
        try {
//            return dao.getByMachineType(machineType.toString())
            return emptyList()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return emptyList()
    }

    suspend fun getAvailableServiceByJobOrder(jobOrderId: String?, machineType: MachineType?) : List<EntityAvailableService> {
        try {
//            return dao.getAvailableWashes(jobOrderId, machineType.toString())
            return emptyList()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return emptyList()
    }
}