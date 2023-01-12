package com.csi.palabakosys.app.joborders.create.services

import android.os.Parcelable
import com.csi.palabakosys.model.MachineType
import com.csi.palabakosys.model.WashType
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
class MenuServiceItem(
    val id: String,
    val name: String,
    val minutes: Int?,
    val price: Float,
    val machineType: MachineType?,
    val washType: WashType?,
    var quantity: Int = 0,
) : Parcelable {
    @IgnoredOnParcel
    var selected = false
    fun abbr() : String {
        return if(machineType != null) {
            machineType.abbr + ' ' + name
        } else {
            name
        }
    }
    fun quantityStr() : String {
        return quantity.toString() + if(quantity == 1) {
            " load"
        } else if(quantity == 0) {
            ""
        } else {
            " loads"
        }
    }
}