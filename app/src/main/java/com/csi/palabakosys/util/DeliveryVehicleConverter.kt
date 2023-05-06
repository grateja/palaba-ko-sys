package com.csi.palabakosys.util

import androidx.room.TypeConverter
import com.csi.palabakosys.model.DeliveryVehicle

object DeliveryVehicleConverter {
    @TypeConverter
    fun fromDeliveryVehicle(deliveryVehicle: DeliveryVehicle?): String? {
        return deliveryVehicle?.toString()
    }

    @TypeConverter
    fun deliveryVehicleFromString(string: String?): DeliveryVehicle? {
        return if(string == null) {
            null
        } else {
            DeliveryVehicle.fromString(string)
        }
    }
}