package com.csi.palabakosys.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.csi.palabakosys.model.MachineType
import com.csi.palabakosys.model.WashType

@Entity(tableName = "wash_services")
class EntityServiceWash(
    @ColumnInfo(name = "name")
    var name: String?,

    @ColumnInfo(name = "minutes")
    var minutes: Int,

    @ColumnInfo(name = "price")
    var price: Float,

    @ColumnInfo(name = "machine_type")
    var machineType: MachineType?,

    @ColumnInfo(name = "wash_type")
    var washType: WashType?,
) : BaseEntity()
