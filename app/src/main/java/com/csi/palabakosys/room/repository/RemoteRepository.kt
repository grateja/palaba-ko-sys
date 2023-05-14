package com.csi.palabakosys.room.repository

import com.csi.palabakosys.room.dao.DaoRemote
import com.csi.palabakosys.room.entities.EntityActivationRef
import com.csi.palabakosys.room.entities.EntityJobOrderService
import com.csi.palabakosys.room.entities.EntityMachine
import com.csi.palabakosys.room.entities.EntityMachineUsage
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
}