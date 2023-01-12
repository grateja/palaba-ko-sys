package com.csi.palabakosys.util

import androidx.room.TypeConverter
import com.csi.palabakosys.model.ServiceType

object ServiceTypeConverter {
    @TypeConverter
    fun fromServiceType(serviceType: ServiceType?): String? {
        return serviceType?.toString()
    }

    @TypeConverter
    fun serviceTypeFromString(string: String?): ServiceType? {
        return if(string == null) {
            null
        } else {
            ServiceType.fromString(string)
        }
    }
}