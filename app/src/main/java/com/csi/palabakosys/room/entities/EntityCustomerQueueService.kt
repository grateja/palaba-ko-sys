package com.csi.palabakosys.room.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import com.csi.palabakosys.model.EnumMachineType
import kotlinx.parcelize.Parcelize
import java.time.Instant
import java.util.*

@Parcelize
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

    @ColumnInfo(name = "latest_job_order")
    val latestJobOrderDate: Instant?,
) : Parcelable