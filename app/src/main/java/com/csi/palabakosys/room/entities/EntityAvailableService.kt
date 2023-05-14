package com.csi.palabakosys.room.entities

import androidx.room.ColumnInfo
import androidx.room.Relation
import com.csi.palabakosys.model.EnumMachineType
import com.csi.palabakosys.model.EnumWashType
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
    val washType: EnumWashType?,

    @ColumnInfo(name = "machine_type")
    val machineType: EnumMachineType?,

    @Relation(
        parentColumn = "id",
        entityColumn = "id"
    )
    var service: EntityJobOrderService
)