package com.csi.palabakosys.room.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Ignore
import com.csi.palabakosys.model.MachineType
import com.csi.palabakosys.model.WashType
import kotlinx.parcelize.Parcelize
import java.util.*

class EntityServiceRef(
    @ColumnInfo(name = "svc_machine_type")
    var machineType: MachineType,

    @ColumnInfo(name = "svc_wash_type")
    var washType: WashType?,

    @ColumnInfo(name = "svc_minutes")
    var minutes: Int,
) {
    fun pulse() : Int {
        return if(machineType == MachineType.REGULAR_DRYER || machineType == MachineType.TITAN_DRYER) {
            minutes / 10
        } else {
            washType?.pulse ?: 0
        }
    }
}