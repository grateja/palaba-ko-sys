package com.csi.palabakosys.room.entities

import androidx.room.ColumnInfo
import java.time.Instant
import java.util.*

data class EntityActivationRef(
    @ColumnInfo(name = "time_activated")
    var timeActivated: Instant?,

    @ColumnInfo(name = "total_minutes")
    var totalMinutes: Int?,

    @ColumnInfo(name = "customer_id")
    var customerId: String?,

    @ColumnInfo(name = "job_order_Id")
    var jobOrderId: UUID?,

    @ColumnInfo(name = "worker_id")
    var workerId: UUID?
)