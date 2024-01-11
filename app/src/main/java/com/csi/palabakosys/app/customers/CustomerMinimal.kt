package com.csi.palabakosys.app.customers

import android.os.Parcelable
import androidx.room.ColumnInfo
import kotlinx.parcelize.Parcelize
import java.time.Instant
import java.util.UUID

@Parcelize
data class CustomerMinimal(
    var id: UUID,
    var name: String,
    var crn: String,
    var address: String?,
    var unpaid: Int?,

    @ColumnInfo(name ="has_unpaid_jo_today")
    var hasUnpaidJoToday: Boolean,

    @ColumnInfo(name = "last_job_order")
    var lastJobOrder: Instant?,
) : Parcelable