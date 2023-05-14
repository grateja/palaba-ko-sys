package com.csi.palabakosys.util.converters

import androidx.room.TypeConverter
import com.csi.palabakosys.model.EnumServiceType

object ServiceTypeConverter {
    @TypeConverter
    fun fromServiceType(serviceType: EnumServiceType?): Int? {
        return serviceType?.id
    }

    @TypeConverter
    fun serviceTypeFromInt(id: Int?): EnumServiceType? {
        return if(id == null) {
            null
        } else {
            EnumServiceType.fromId(id)
        }
    }
}