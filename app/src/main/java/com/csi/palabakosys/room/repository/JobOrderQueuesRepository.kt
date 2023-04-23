package com.csi.palabakosys.room.repository

import com.csi.palabakosys.model.MachineType
import com.csi.palabakosys.room.dao.DaoJobOrderQueues
import com.csi.palabakosys.room.entities.EntityAvailableService
import com.csi.palabakosys.room.entities.EntityCustomerQueueService
import com.csi.palabakosys.room.entities.EntityJobOrderService
import java.lang.Exception
import javax.inject.Inject

class JobOrderQueuesRepository
@Inject
constructor (
    private val dao: DaoJobOrderQueues,
) {
    suspend fun getByMachineType(machineType: MachineType?) : List<EntityCustomerQueueService> {
        try {
            return dao.getByMachineType(machineType.toString())
//            return emptyList()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return emptyList()
    }

    suspend fun getAvailableServiceByCustomerId(customerId: String?, machineType: MachineType?) : List<EntityAvailableService> {
        try {
            return dao.getAvailableWashes(customerId, machineType.toString())
//            return emptyList()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return emptyList()
    }

    suspend fun get(id: String?) : EntityJobOrderService? {
        return dao.get(id)
    }
}