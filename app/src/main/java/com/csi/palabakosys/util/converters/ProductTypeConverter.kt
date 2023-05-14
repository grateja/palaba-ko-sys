package com.csi.palabakosys.util.converters

import androidx.room.TypeConverter
import com.csi.palabakosys.model.EnumProductType

object ProductTypeConverter {
    @TypeConverter
    fun fromProductType(productType: EnumProductType?): Int? {
        return productType?.id
    }

    @TypeConverter
    fun productTypeFromId(id: Int?): EnumProductType? {
        return if(id == null) {
            null
        } else {
            EnumProductType.fromId(id)
        }
    }
}