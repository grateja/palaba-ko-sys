package com.csi.palabakosys.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import java.util.*

@Entity(tableName = "machine_usages")
class EntityMachineUsage : BaseEntity() {
    @ColumnInfo(name = "machine_id")
    var machineId: UUID? = null

    @ColumnInfo(name = "customer_name")
    var customerName: String? = null
    var minutes: Int = 0
    var price: Float = 0f
}