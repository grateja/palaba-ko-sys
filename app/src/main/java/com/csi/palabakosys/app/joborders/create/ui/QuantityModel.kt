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
        val TYPE_SERVICE: String = "service"
        val TYPE_PRODUCT: String = "product"
    }
}