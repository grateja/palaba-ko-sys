package com.csi.palabakosys.room.entities

import androidx.room.ColumnInfo
import com.csi.palabakosys.model.MachineType
import java.util.*

data class EntityCustomerQueueService (
    @ColumnInfo(name = "job_order_id")
    val jobOrderId: UUID,
    @ColumnInfo(name = "job_order_number")
    val jobOrderNumber: String,
    @ColumnInfo(name = "customer_name")
    val customerName: String
)