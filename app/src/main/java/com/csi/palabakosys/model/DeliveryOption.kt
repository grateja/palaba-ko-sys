package com.csi.palabakosys.model

import com.csi.palabakosys.R

enum class DeliveryOption(val id: Int, val value: String, val charge: Int) {
    DELIVERY_ONLY(R.id.deliverOptionDeliveryOnly, "Delivery Only", 1),
    PICKUP_ONLY(R.id.deliverOptionPickupOnly,"Pickup Only", 1),
    PICKUP_AND_DELIVERY(R.id.deliverOptionPickupAndDelivery,"Pickup & Delivery", 2);

    override fun toString() : String {
        return value
    }

    companion object {
        fun fromId(id: Int) : DeliveryOption {
            return values().find {
                it.id == id
            } ?: PICKUP_AND_DELIVERY
        }
    }
}