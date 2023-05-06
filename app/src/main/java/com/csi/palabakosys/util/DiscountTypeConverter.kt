//package com.csi.palabakosys.util
//
//import androidx.room.TypeConverter
//import com.csi.palabakosys.model.DiscountTypeEnum
//
//object DiscountTypeConverter {
//    @TypeConverter
//    fun fromDiscountType(discountApplicable: DiscountTypeEnum?): String? {
//        return discountApplicable?.toString()
//    }
//
//    @TypeConverter
//    fun toDiscountApplicable(string: String?): DiscountTypeEnum? {
//        return if(string == null) {
//            null
//        } else {
//            DiscountTypeEnum.fromString(string)
//        }
//    }
//}