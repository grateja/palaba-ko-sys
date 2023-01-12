package com.csi.palabakosys.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import java.util.*

@Entity(tableName = "machine_remarks")
class EntityMachineRemarks : BaseEntity() {
    @ColumnInfo(name = "machine_id")
    var machineId: UUID? = null

    @ColumnInfo(name = "staff_name")
    var staffName: String? = null
    var title: String? = null
    var remarks: String? = null
}