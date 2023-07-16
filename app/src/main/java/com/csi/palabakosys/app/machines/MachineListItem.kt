package com.csi.palabakosys.app.machines

import androidx.room.Embedded
import androidx.room.Relation
import com.csi.palabakosys.room.entities.EntityCustomer
import com.csi.palabakosys.room.entities.EntityJobOrderService
import com.csi.palabakosys.room.entities.EntityMachine
import java.time.Instant

data class MachineListItem(
    @Embedded
    val machine: EntityMachine,

    @Relation(
        parentColumn = "customer_id",
        entityColumn = "id"
    )
    val customer: EntityCustomer?,

    @Relation(
        parentColumn = "jo_service_id",
        entityColumn = "id"
    )
    val service: EntityJobOrderService?,
)