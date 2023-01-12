package com.csi.palabakosys.util

import androidx.room.TypeConverter
import com.csi.palabakosys.model.PaymentMethodEnum

object PaymentMethodConverter {
    @TypeConverter
    fun fromPaymentMethod(paymentMethod: PaymentMethodEnum?): String? {
        return paymentMethod?.toString()
    }

    @TypeConverter
    fun paymentMethodFromString(string: String?): PaymentMethodEnum? {
        return if(string == null) {
            null
        } else {
            PaymentMethodEnum.fromString(string)
        }
    }
}