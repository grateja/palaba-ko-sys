package com.csi.palabakosys.app.joborders.create.extras

import android.os.Parcelable
import androidx.room.Ignore
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
class MenuExtrasItem(
    val id: UUID,
    val name: String,
    val price: Float,
    val category: String?,
    var quantity: Int = 1,
) : Parcelable {
    @IgnoredOnParcel
    @Ignore
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