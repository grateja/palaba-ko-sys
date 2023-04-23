package com.csi.palabakosys.room.entities

import androidx.room.ColumnInfo
import androidx.room.Relation
import com.csi.palabakosys.model.MachineType
import com.csi.palabakosys.model.WashType
import java.util.*

data class EntityAvailableService(
    val id: UUID?,
    val available: Int,
    val minutes: Int,

    @ColumnInfo(name = "service_name")
    val serviceName: String?,

    @ColumnInfo(name = "service_id")
    val serviceId: UUID?,

    @ColumnInfo(name = "job_order_id")
    val jobOrderId: UUID?,

    @ColumnInfo(name = "wash_type")
    val washType: WashType?,

    @ColumnInfo(name = "machine_type")
    val machineType: MachineType?,

    @Relation(
        parentColumn = "id",
        entityColumn = "id"
    )
    var service: EntityJobOrderService
)