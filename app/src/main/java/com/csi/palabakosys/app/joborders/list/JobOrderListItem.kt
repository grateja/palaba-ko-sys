package com.csi.palabakosys.app.joborders.list

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Ignore
import kotlinx.parcelize.Parcelize
import java.time.Instant
import java.util.*

@Parcelize
data class JobOrderListItem(
    val id: UUID,

    @ColumnInfo("payment_id")
    val paymentId: String?,

    @ColumnInfo("customer_id")
    val customerId: String,

    @ColumnInfo("name")
    val customerName: String,

    @ColumnInfo("crn")
    val crn: String,

    @ColumnInfo("job_order_number")
    val jobOrderNumber: String,

    @ColumnInfo("discounted_amount")
    val discountedAmount: Float,

    @ColumnInfo("created_at")
    val createdAt: Instant,

    @ColumnInfo("date_paid")
    val datePaid: Instant?,
) : Parcelable {
    fun paymentStatus() : String {
        return if(datePaid == null)
            "UNPAID"
        else
            "PAID"
    }
}