package com.csi.palabakosys.room.entities

import androidx.room.*
import java.util.*

@Entity(tableName = "job_orders")
open class EntityJobOrder(
    @ColumnInfo(name = "job_order_number")
    var jobOrderNumber: String?,

    @ColumnInfo(name = "customer_id")
    var customerId: UUID?,

    @ColumnInfo(name = "customer_name")
    var customerName: String?,

    @ColumnInfo(name = "prepared_by")
    var preparedBy: String?
) : BaseEntity() {
    @Embedded
    var entityJobOrderVoid: EntityJobOrderVoid? = null
}