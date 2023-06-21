package com.csi.palabakosys.room.entities

import androidx.room.ColumnInfo
import com.csi.palabakosys.model.EnumMachineType
import java.util.*

data class EntityCustomerQueueService (
    @ColumnInfo(name = "customer_name")
    val customerName: String,

    @ColumnInfo(name = "customer_id")
    val customerId: UUID,

    @ColumnInfo(name = "crn")
    val crn: String,

    @ColumnInfo(name = "address")
    val address: String?,

    @ColumnInfo(name = "machine_type")
    val machineType: EnumMachineType,
)