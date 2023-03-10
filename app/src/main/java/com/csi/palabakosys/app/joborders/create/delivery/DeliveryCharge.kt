package com.csi.palabakosys.app.joborders.create.delivery

import android.os.Parcelable
import com.csi.palabakosys.model.DeliveryOption
import kotlinx.parcelize.Parcelize

@Parcelize
class DeliveryCharge(
    var deliveryProfile: MenuDeliveryProfile,
    var distance: Float = deliveryProfile.minDistance,
    var deliveryOption: DeliveryOption = DeliveryOption.PICKUP_AND_DELIVERY
) : Parcelable {
    fun price() : Float {
        return deliveryOption.charge * ((deliveryProfile.pricePerKm * distance) + deliveryProfile.baseFare)
    }
}