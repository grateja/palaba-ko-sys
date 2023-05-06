package com.csi.palabakosys.app.joborders.create.delivery

import android.os.Parcelable
import com.csi.palabakosys.model.DeliveryOption
import com.csi.palabakosys.model.DeliveryVehicle
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
class DeliveryCharge(
    var deliveryProfileId: UUID,
    var vehicle: DeliveryVehicle,
    var distance: Float,
    var deliveryOption: DeliveryOption = DeliveryOption.PICKUP_AND_DELIVERY,
    var price: Float,
//    var joDeliveryId: UUID? = null,
) : Parcelable {
//    fun price() : Float {
//        return deliveryOption.charge * ((deliveryProfile.pricePerKm * distance) + deliveryProfile.baseFare)
//    }
}