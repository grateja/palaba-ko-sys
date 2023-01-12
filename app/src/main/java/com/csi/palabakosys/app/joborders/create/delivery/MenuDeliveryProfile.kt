package com.csi.palabakosys.app.joborders.create.delivery

import android.os.Parcelable
import com.csi.palabakosys.model.DeliveryOption
import com.csi.palabakosys.model.DeliveryVehicle
import kotlinx.parcelize.Parcelize

@Parcelize
class MenuDeliveryProfile(
    val vehicle: DeliveryVehicle,
    val baseFare: Float,
    val pricePerKm: Float = 0f,
    val minDistance: Float = 1f,
    var selected: Boolean = false
) : Parcelable