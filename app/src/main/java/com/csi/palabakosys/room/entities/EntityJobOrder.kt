package com.csi.palabakosys.room.entities

import androidx.room.*
import java.util.*

@Entity(tableName = "job_orders")
open class EntityJobOrder(
    @ColumnInfo(name = "job_order_number")
    var jobOrderNumber: String?
) : BaseEntity() {
    @ColumnInfo(name = "customer_id")
    var customerId: UUID? = null

    @ColumnInfo(name = "customer_name")
    var customerName: String? = null

    @ColumnInfo(name = "prepared_by")
    var preparedBy: String? = null

    @Embedded
    var entityJobOrderVoid: EntityJobOrderVoid? = null
}