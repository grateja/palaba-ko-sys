package com.csi.palabakosys.room.entities

import androidx.room.Embedded
import androidx.room.Relation

data class EntityRunningMachine(
    @Embedded var machine: EntityMachine,

    @Relation(
        parentColumn = "jo_service_id",
        entityColumn = "id"
    )
    var jobOrderService: EntityJobOrderService? = null,

    @Relation(
        parentColumn = "id",
        entityColumn = "machine_id"
    )
    var machineUsage: EntityMachineUsage
)