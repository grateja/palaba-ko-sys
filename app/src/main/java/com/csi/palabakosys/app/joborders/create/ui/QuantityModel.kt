package com.csi.palabakosys.app.joborders.create.ui

import android.os.Parcelable
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
class QuantityModel(
    val id: String,
    val name: String,
    var quantity: Int = 0,
    val type: String
) : Parcelable {
    companion object {
        const val TYPE_EXTRAS: String = "extras"
        const val TYPE_SERVICE: String = "service"
        const val TYPE_PRODUCT: String = "product"
    }
}