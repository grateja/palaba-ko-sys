package com.csi.palabakosys.app.joborders.create.delivery

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Ignore
import com.csi.palabakosys.model.EnumDeliveryOption
import com.csi.palabakosys.model.EnumDeliveryVehicle
import kotlinx.parcelize.Parcelize
import java.time.Instant
import java.util.*

@Parcelize
class DeliveryCharge(
    var deliveryProfileId: UUID,
    var vehicle: EnumDeliveryVehicle,
    var distance: Float,
    var deliveryOption: EnumDeliveryOption = EnumDeliveryOption.PICKUP_AND_DELIVERY,
    var price: Float,
    var deletedAt: Instant?,
) : Parcelable {

//    fun price() : Float {
//        return deliveryOption.charge * ((deliveryProfile.pricePerKm * distance) + deliveryProfile.baseFare)
//    }
}