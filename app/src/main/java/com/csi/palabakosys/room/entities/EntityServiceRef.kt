package com.csi.palabakosys.room.entities

import androidx.room.ColumnInfo
import com.csi.palabakosys.model.MachineType
import com.csi.palabakosys.model.WashType
import java.util.*

class EntityServiceRef(
    @ColumnInfo(name = "svc_machine_type")
    var machineType: MachineType,

    @ColumnInfo(name = "svc_wash_type")
    var washType: WashType?,

    @ColumnInfo(name = "svc_minutes")
    var minutes: Int,
)