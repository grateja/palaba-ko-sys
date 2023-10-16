package com.csi.palabakosys.app.dashboard.data

import com.csi.palabakosys.app.machines.MachineListItem
import com.csi.palabakosys.room.entities.EntityJobStatus

data class DashboardJobStatus(
    val jobStatus: EntityJobStatus?,
    val runningMachines: List<MachineListItem>?
)