package com.csi.palabakosys.room.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Ignore
import java.util.*

@Entity(tableName = "job_order_services")
data class EntityJobOrderService(
    @ColumnInfo(name = "job_order_id")
    var jobOrderId: UUID?,

//    @ColumnInfo(name = "machine_type")
//    var machineType: MachineType?,
//
    @ColumnInfo(name = "service_id")
    var serviceId: UUID,

    @ColumnInfo(name = "service_name")
    var serviceName: String,

    @ColumnInfo(name = "price")
    var price: Float,

    @ColumnInfo(name = "quantity")
    var quantity: Int,

    @Embedded
    var service: EntityServiceRef,

    @Ignore
    @ColumnInfo
    var uuid: UUID? = null
) : BaseEntity(uuid) {
    constructor(jobOrderId: UUID?, serviceId: UUID, serviceName: String, price: Float, quantity: Int, service: EntityServiceRef) :
            this(jobOrderId, serviceId, serviceName, price, quantity, service, null)

    @ColumnInfo(name = "used")
    var used: Int = 0

    @ColumnInfo(name = "cash_back")
    var cashBack: Float = 0f
}
