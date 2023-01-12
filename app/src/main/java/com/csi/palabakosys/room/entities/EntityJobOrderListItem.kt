package com.csi.palabakosys.room.entities

import androidx.room.ColumnInfo
import java.time.Instant

class EntityJobOrderListItem (
    jobOrderNumber: String?
) : EntityJobOrder(jobOrderNumber) {
    @ColumnInfo(name = "total_amount")
    var totalAmount: Float = 0f

    @ColumnInfo(name = "date_paid")
    var datePaid: Instant? = null

    var balance: Float = 0f
}