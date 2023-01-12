package com.csi.palabakosys.util

import androidx.room.TypeConverter
import com.csi.palabakosys.model.ProductType

object ProductTypeConverter {
    @TypeConverter
    fun fromProductType(productType: ProductType?): String? {
        return productType?.toString()
    }

    @TypeConverter
    fun productTypeFromString(string: String?): ProductType? {
        return if(string == null) {
            null
        } else {
            ProductType.fromString(string)
        }
    }
}