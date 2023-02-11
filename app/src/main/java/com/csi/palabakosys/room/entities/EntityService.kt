package com.csi.palabakosys.room.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity

@Entity(tableName = "services")
class EntityService(
    @ColumnInfo(name = "name")
    var name: String?,

//    @ColumnInfo(name = "minutes")
//    var minutes: Int,

    @ColumnInfo(name = "price")
    var price: Float,

    @Embedded
    var service: EntityServiceRef

//    @ColumnInfo(name = "machine_type")
//    var machineType: MachineType?,
//
//    @ColumnInfo(name = "wash_type")
//    var washType: WashType?,
) : BaseEntity()
