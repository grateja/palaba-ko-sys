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
//    suspend fun getByMachineType(machineType: EnumMachineType) : List<EntityCustomerQueueService> {
//        try {
//            return dao.getCustomersByMachineType(machineType)
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//        return emptyList()
//    }
    fun getByMachineType(machineType: EnumMachineType?) = dao.getCustomersByMachineType(machineType)

    suspend fun getAvailableServiceByCustomerId(customerId: UUID, machineType: EnumMachineType) : List<EntityAvailableService> {
        try {
            return dao.getAvailableWashes(customerId, machineType)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return emptyList()
    }

    fun getAvailableServicesByCustomerIdAsLiveData(customerId: UUID, machineType: EnumMachineType) = dao.getAvailableServicesAsLiveData(customerId, machineType)

    fun getAsLiveData(id: UUID?) = dao.getAsLiveData(id)

    suspend fun get(id: UUID?) : EntityJobOrderService? {
        return dao.get(id)
    }
}