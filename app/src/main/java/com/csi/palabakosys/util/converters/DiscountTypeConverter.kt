package com.csi.palabakosys.util.converters

import androidx.room.TypeConverter
import com.csi.palabakosys.model.EnumDiscountType

object DiscountTypeConverter {
    @TypeConverter
    fun fromDiscountType(discountApplicable: EnumDiscountType?): String? {
        return discountApplicable?.toString()
    }

    @TypeConverter
    fun toDiscountApplicable(string: String?): EnumDiscountType? {
        return if(string == null) {
            null
        } else {
            EnumDiscountType.fromString(string)
        }
    }
}