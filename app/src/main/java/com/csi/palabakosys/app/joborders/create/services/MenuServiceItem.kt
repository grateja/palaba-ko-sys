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
    val id: UUID,
    val name: String,
    val minutes: Int?,
    val price: Float,

    @ColumnInfo(name = "machine_type")
    val machineType: MachineType?,

    @ColumnInfo(name = "wash_type")
    val washType: WashType?,
    var quantity: Int = 1,
) : Parcelable {
    @IgnoredOnParcel
    @Ignore
    var selected = false
    fun abbr() : String {
        return if(machineType != null) {
            machineType.abbr + ' ' + name
        } else {
            name
        }
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