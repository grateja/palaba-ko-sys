package com.csi.palabakosys.util.converters

import androidx.room.TypeConverter
import com.csi.palabakosys.model.EnumDeliveryVehicle

object DeliveryVehicleConverter {
    @TypeConverter
    fun fromDeliveryVehicle(deliveryVehicle: EnumDeliveryVehicle?): Int? {
        return deliveryVehicle?.id
    }

    @TypeConverter
    fun deliveryVehicleFromInt(id: Int?): EnumDeliveryVehicle? {
        return if(id == null) {
            null
        } else {
            EnumDeliveryVehicle.fromId(id)
        }
    }
}