package com.csi.palabakosys.app.joborders.create.ui

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class RemoveItemModel(
    val id: String,
    val name: String,
    val type: String,
    val quantity: Int
): Parcelable {
    companion object {
        val TYPE_SERVICE: String = "service"
        val TYPE_PRODUCT: String = "product"
    }

    fun label() : String {
        return "(*$quantity) $name"
    }
}