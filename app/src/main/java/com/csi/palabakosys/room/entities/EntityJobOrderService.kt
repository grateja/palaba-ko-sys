package com.csi.palabakosys.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.csi.palabakosys.model.MachineType
import java.util.*

@Entity(tableName = "job_order_services")
data class EntityJobOrderService(
    @ColumnInfo(name = "job_order_id")
    var jobOrderId: UUID?,

    @ColumnInfo(name = "machine_type")
    var machineType: MachineType?,

    @ColumnInfo(name = "service_id")
    var serviceId: UUID?
) : BaseEntity() {
    @ColumnInfo(name = "service_name")
    var serviceName: String? = null

    @ColumnInfo(name = "price")
    var price: Float = 0f

    @ColumnInfo(name = "quantity")
    var quantity: Int = 1

    @ColumnInfo(name = "used")
    var used: Int = 0

    @ColumnInfo(name = "cash_back")
    var cashBack: Float = 0f
}
