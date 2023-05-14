package com.csi.palabakosys.room.repository

import com.csi.palabakosys.model.EnumMachineType
import com.csi.palabakosys.room.dao.DaoJobOrderQueues
import com.csi.palabakosys.room.entities.EntityAvailableService
import com.csi.palabakosys.room.entities.EntityCustomerQueueService
import com.csi.palabakosys.room.entities.EntityJobOrderService
import java.lang.Exception
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JobOrderQueuesRepository
@Inject
constructor (
    private val dao: DaoJobOrderQueues,
) {
    suspend fun getByMachineType(machineType: EnumMachineType) : List<EntityCustomerQueueService> {
        try {
            return dao.getByMachineType(machineType)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return emptyList()
    }

    suspend fun getAvailableServiceByCustomerId(customerId: UUID, machineType: EnumMachineType) : List<EntityAvailableService> {
        try {
            return dao.getAvailableWashes(customerId, machineType)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return emptyList()
    }

    suspend fun get(id: UUID) : EntityJobOrderService? {
        return dao.get(id)
    }
}