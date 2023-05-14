package com.csi.palabakosys.util

import androidx.room.TypeConverter
import com.csi.palabakosys.model.ActionPermissionEnum
import com.csi.palabakosys.model.EnumDiscountApplicable

object ActionPermissionConverter {
    @TypeConverter
    fun fromActionPermission(permissions: List<ActionPermissionEnum>): String {
        return ActionPermissionEnum.toIds(permissions)
    }

    @TypeConverter
    fun toActionPermission(string: String?): List<ActionPermissionEnum> {
        return if(string == null) {
            listOf()
        } else {
            ActionPermissionEnum.fromIds(string)
        }
    }
}