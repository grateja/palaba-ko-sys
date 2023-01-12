package com.csi.palabakosys.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.csi.palabakosys.model.MachineType

@Entity(tableName = "dry_services")
class EntityServiceDry : BaseEntity() {
    @ColumnInfo(name = "machine_type")
    var machineType: MachineType? = null

    @ColumnInfo(name = "name")
    var name: String? = null

    @ColumnInfo(name = "price")
    var price: Float = 0f

    @ColumnInfo(name = "minutes")
    var minutes: Int = 0

    @ColumnInfo(name = "cash_back")
    var cashBack: Float = 0f
}