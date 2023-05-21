package com.csi.palabakosys.room.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import com.csi.palabakosys.model.EnumMachineType
import java.util.*

@Entity(tableName = "machines")
data class EntityMachine(
    @ColumnInfo(name = "stack_order")
    var stackOrder: Int?,

    @ColumnInfo(name = "machine_type")
    var machineType: EnumMachineType?,

    @ColumnInfo(name = "ip_end")
    var ipEnd: Int,

    @ColumnInfo(name = "machine_number")
    var machineNumber: Int,

    @Embedded
    var activationRef: EntityActivationRef? = null
) : BaseEntity() {

    fun machineName() : String {
        return machineType?.value + "" + machineNumber
    }
//    @ColumnInfo(name = "time_activated")
//    var timeActivated: Instant? = null
//
//    @ColumnInfo(name = "total_minutes")
//    var totalMinutes: Int = 0
//
//    @ColumnInfo(name = "customer_name")
//    var customerName: String? = null
//
//    @ColumnInfo(name = "customer_wash_id")
//    var customerWashId: UUID? = null
//
//    @ColumnInfo(name = "customer_dry_id")
//    var customerDryId: UUID? = null
//
    @ColumnInfo(name = "service_activation_id")
    var serviceActivationId: UUID? = null
}
