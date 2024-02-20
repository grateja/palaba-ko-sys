package com.csi.palabakosys.app.customers

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Ignore
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import java.time.Instant
import java.util.UUID

@Parcelize
data class CustomerMinimal(
    var id: UUID,
    var name: String,
    var crn: String,
    var address: String?,
    var unpaid: Int,

    @ColumnInfo(name ="unpaid_jo_id_today")
    var hasUnpaidJoToday: UUID?,

    @ColumnInfo(name = "last_job_order")
    var lastJobOrder: Instant?,

    var selected: Boolean? = false,

    @ColumnInfo(name = "jo_date")
    var joDate: String? = null,
    @ColumnInfo(name = "date_now")
    var dateNow: String? = null
) : Parcelable