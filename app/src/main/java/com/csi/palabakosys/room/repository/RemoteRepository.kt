package com.csi.palabakosys.room.repository

import com.csi.palabakosys.room.dao.DaoRemote
import com.csi.palabakosys.room.entities.*
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteRepository
@Inject
constructor (
    private val daoRemote: DaoRemote
) {
    suspend fun activate(
        activationRef: EntityActivationRef, jobOrderServiceId: UUID, machineId: UUID, machineUsage: EntityMachineUsage
    ) {
        daoRemote.activate(activationRef, jobOrderServiceId, machineId, machineUsage)
    }

    suspend fun preActivate(machineId: UUID, jobOrderServiceId: UUID) {
        daoRemote.preActivate(machineId, jobOrderServiceId)
    }

    suspend fun revertActivation(machineId: UUID, jobOrderServiceId: UUID) {
        daoRemote.cancelActivation(machineId, jobOrderServiceId)
    }

    suspend fun getRunningMachine(machineId: UUID?) : EntityRunningMachine? {
        return daoRemote.getActiveMachine(machineId)
    }
}