//package com.csi.palabakosys.util
//
//import androidx.room.TypeConverter
//import com.csi.palabakosys.model.DiscountApplicable
//
//object DiscountApplicableConverter {
//    @TypeConverter
//    fun fromDiscountApplicable(discountApplicable: DiscountApplicable?): String? {
//        return discountApplicable?.toString()
//    }
//
//    @TypeConverter
//    fun toDiscountApplicable(string: String?): DiscountApplicable? {
//        return if(string == null) {
//            null
//        } else {
//            DiscountApplicable.fromString(string)
//        }
//    }
//}