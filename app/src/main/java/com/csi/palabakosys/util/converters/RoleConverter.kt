package com.csi.palabakosys.util.converters

import androidx.room.TypeConverter
import com.csi.palabakosys.model.Role

object RoleConverter {
    @TypeConverter
    fun fromRole(role: Role?): String? {
        return role?.toString()
    }

    @TypeConverter
    fun roleFromString(string: String?): Role? {
        return if(string == null) {
            null
        } else {
            Role.fromString(string)
        }
    }
}