package com.csi.palabakosys.app.joborders.create.delivery

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Ignore
import com.csi.palabakosys.model.DeliveryOption
import com.csi.palabakosys.model.DeliveryVehicle
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
class MenuDeliveryProfile(
//    val joDeliveryProfileId: UUID?,

    @ColumnInfo(name = "id")
    val deliveryProfileRefId: UUID,

    val vehicle: DeliveryVehicle,

    @ColumnInfo(name = "base_fare")
    val baseFare: Float,

    @ColumnInfo(name = "price_per_km")
    val pricePerKm: Float = 0f,

    @ColumnInfo(name = "min_distance")
    val minDistance: Float = 1f,
) : Parcelable {
    @IgnoredOnParcel
    @Ignore
    var selected: Boolean = false
}