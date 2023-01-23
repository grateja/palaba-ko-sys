package com.csi.palabakosys.app.joborders.create.extras

import android.os.Parcelable
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
class MenuExtrasItem(
    val id: String,
    val name: String,
    val price: Float,
    val category: String,
    var quantity: Int = 0,
) : Parcelable {
    @IgnoredOnParcel
    var selected = false

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