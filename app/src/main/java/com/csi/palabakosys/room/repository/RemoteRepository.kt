package com.csi.palabakosys.room.repository

import com.csi.palabakosys.room.dao.DaoRemote
import com.csi.palabakosys.room.entities.EntityJobOrderService
import com.csi.palabakosys.room.entities.EntityMachine
import com.csi.palabakosys.room.entities.EntityMachineUsage
import javax.inject.Inject

class RemoteRepository
@Inject
constructor (
    private val daoRemote: DaoRemote
) {
    suspend fun activate(
        machine: EntityMachine, jobOrderService: EntityJobOrderService, machineUsage: EntityMachineUsage
    ) {
//        daoRemote.activate(machine, jobOrderService, machineUsage)
    }
}