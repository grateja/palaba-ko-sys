package com.csi.palabakosys.app.remote.panel

import com.csi.palabakosys.room.entities.EntityMachine

class MachineTile(
    val machine: EntityMachine,
) {
    fun machineName() : String {
        return machine.machineType?.value + "" + machine.machineNumber
    }
}