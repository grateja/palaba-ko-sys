package com.csi.palabakosys.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import java.util.*

@Entity(tableName = "machine_usages")
class EntityMachineUsage(
    @ColumnInfo(name = "machine_id")
    val machineId: UUID? = null,

    @ColumnInfo(name = "job_order_service_id")
    val jobOrderServiceId: UUID? = null,
) : BaseEntity() {
//    @ColumnInfo(name = "customer_name")
//    var customerName: String? = null
//    var minutes: Int = 0
//    var price: Float = 0f
}