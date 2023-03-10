package com.csi.palabakosys.room.entities

import androidx.room.ColumnInfo
import java.time.Instant
import java.util.*

class EntityJobOrderListItem (
    jobOrderNumber: String?,
    customerId: UUID?,
    customerName: String?,
    preparedBy: String?
) : EntityJobOrder(jobOrderNumber, customerId, customerName, preparedBy) {
    @ColumnInfo(name = "total_amount")
    var totalAmount: Float = 0f

    @ColumnInfo(name = "date_paid")
    var datePaid: Instant? = null

    var balance: Float = 0f
}