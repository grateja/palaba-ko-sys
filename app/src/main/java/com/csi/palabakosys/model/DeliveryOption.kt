package com.csi.palabakosys.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class DeliveryOption(val value: String, val charge: Int, var selected: Boolean = false) : Parcelable {
    DELIVERY_ONLY("Delivery Only", 1),
    PICKUP_ONLY("Pickup Only", 1),
    PICKUP_AND_DELIVERY("Pickup & Delivery", 2);

    override fun toString() : String {
        return value
    }

    companion object {
        fun fromString(name: String?) : DeliveryOption? {
            return values().find {
                it.value == name
            }
        }
    }
}