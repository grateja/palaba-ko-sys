package com.csi.palabakosys.util.converters

import androidx.room.TypeConverter
import com.csi.palabakosys.model.EnumActionPermission

object ActionPermissionConverter {
    @TypeConverter
    fun fromActionPermission(permissions: List<EnumActionPermission>): String {
        return EnumActionPermission.toIds(permissions)
    }

    @TypeConverter
    fun toActionPermission(string: String?): List<EnumActionPermission> {
        return if(string == null) {
            listOf()
        } else {
            EnumActionPermission.fromIds(string)
        }
    }
}