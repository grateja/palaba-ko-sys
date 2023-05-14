package com.csi.palabakosys.util.converters

import androidx.room.TypeConverter
import com.csi.palabakosys.model.EnumPaymentMethod

object PaymentMethodConverter {
    @TypeConverter
    fun fromPaymentMethod(paymentMethod: EnumPaymentMethod?): Int? {
        return paymentMethod?.id
    }

    @TypeConverter
    fun paymentMethodFromInt(id: Int?): EnumPaymentMethod? {
        return if(id == null) {
            null
        } else {
            EnumPaymentMethod.fromId(id)
        }
    }
}