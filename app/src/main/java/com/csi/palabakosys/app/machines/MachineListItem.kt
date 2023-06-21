package com.csi.palabakosys.app.machines

import androidx.room.Embedded
import com.csi.palabakosys.room.entities.EntityMachine

data class MachineListItem(
    @Embedded
    val machine: EntityMachine
)