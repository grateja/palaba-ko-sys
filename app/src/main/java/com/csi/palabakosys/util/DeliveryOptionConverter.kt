package com.csi.palabakosys.util

import androidx.room.TypeConverter
import com.csi.palabakosys.model.DeliveryOption

object DeliveryOptionConverter {
    @TypeConverter
    fun fromDeliveryOption(deliveryOption: DeliveryOption?): String? {
        return deliveryOption?.toString()
    }

    @TypeConverter
    fun deliveryOptionFromString(string: String?): DeliveryOption? {
        return if(string == null) {
            null
        } else {
            DeliveryOption.fromString(string)
        }
    }
}