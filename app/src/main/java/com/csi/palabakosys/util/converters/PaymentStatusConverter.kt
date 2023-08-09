package com.csi.palabakosys.util.converters

import androidx.room.TypeConverter
import com.csi.palabakosys.model.EnumPaymentStatus

object PaymentStatusConverter {
    @TypeConverter
    fun fromPaymentStatus(paymentStatus: EnumPaymentStatus?): Int? {
        return paymentStatus?.id
    }

    @TypeConverter
    fun paymentStatusFromInt(id: Int?): EnumPaymentStatus? {
        return if(id == null) {
            null
        } else {
            EnumPaymentStatus.fromId(id)
        }
    }
}