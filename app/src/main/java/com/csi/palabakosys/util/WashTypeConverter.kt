package com.csi.palabakosys.util

import androidx.room.TypeConverter
import com.csi.palabakosys.model.WashType

object WashTypeConverter {
    @TypeConverter
    fun fromWashType(washType: WashType?): String? {
        return washType?.toString()
    }

    @TypeConverter
    fun washTypeFromString(string: String?): WashType? {
        return if(string == null) {
            null
        } else {
            WashType.fromString(string)
        }
    }
}