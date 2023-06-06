package com.csi.palabakosys.room.entities

import androidx.room.*
import java.util.*

@Entity(tableName = "job_order_services")
data class EntityJobOrderService(
    @ColumnInfo(name = "job_order_id")
    var jobOrderId: UUID?,

    @ColumnInfo(name = "service_id")
    var serviceId: UUID,

    @ColumnInfo(name = "service_name")
    var serviceName: String,

    @ColumnInfo(name = "price")
    var price: Float,

    @ColumnInfo(name = "quantity")
    var quantity: Int,

    @ColumnInfo(name = "used")
    var used: Int = 0,

    @Embedded
    var serviceRef: EntityServiceRef,

    @PrimaryKey(autoGenerate = false)
    override var id: UUID,
) : BaseEntity(id)/* {
    constructor(jobOrderId: UUID?, serviceId: UUID, serviceName: String, price: Float, quantity: Int, used: Int, isPackage: Boolean, serviceRef: EntityServiceRef) :
            this(jobOrderId, serviceId, serviceName, price, quantity, used, isPackage, serviceRef, null)
}*/
