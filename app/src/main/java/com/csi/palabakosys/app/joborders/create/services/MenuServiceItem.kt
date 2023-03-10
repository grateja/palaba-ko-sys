package com.csi.palabakosys.app.joborders.create.services

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Ignore
import com.csi.palabakosys.model.MachineType
import com.csi.palabakosys.model.WashType
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
class MenuServiceItem(
    @ColumnInfo(name = "service_id")
    val serviceId: UUID,

    val name: String,

    @ColumnInfo(name = "svc_minutes")
    val minutes: Int,

    val price: Float,

    @ColumnInfo(name = "svc_machine_type")
    val machineType: MachineType,

    @ColumnInfo(name = "svc_wash_type")
    val washType: WashType?,

    var quantity: Int = 1,

    var id: UUID? = null,
) : Parcelable {
    @IgnoredOnParcel
    @Ignore
    var selected = false
    fun abbr() : String {
//        return if(machineType != null) {
            return machineType.abbr + ' ' + name
//        } else {
//            name
//        }
    }
    fun quantityStr() : String {
        return "*$quantity" + if(quantity == 1) {
            " load"
        } else if(quantity == 0) {
            ""
        } else {
            " loads"
        }
    }
}