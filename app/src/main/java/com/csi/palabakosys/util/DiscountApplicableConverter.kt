package com.csi.palabakosys.util

import androidx.room.TypeConverter
import com.csi.palabakosys.model.EnumDiscountApplicable

object DiscountApplicableConverter {
    @TypeConverter
    fun fromDiscountApplicable(discounts: List<EnumDiscountApplicable>): String {
        return EnumDiscountApplicable.toIds(discounts)
    }

    @TypeConverter
    fun toDiscountApplicable(string: String?): List<EnumDiscountApplicable> {
        return if(string == null) {
            listOf()
        } else {
            EnumDiscountApplicable.fromIds(string)
        }
    }
}