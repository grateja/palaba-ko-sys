package com.csi.palabakosys.util.converters

import androidx.room.TypeConverter
import com.csi.palabakosys.model.EnumMachineType

object MachineTypeConverter {
    @TypeConverter
    fun fromMachineType(machineType: EnumMachineType?): Int? {
        return machineType?.id
    }

    @TypeConverter
    fun machineTypeFromInt(id: Int?): EnumMachineType? {
        return if(id == null) {
            null
        } else {
            EnumMachineType.fromId(id)
        }
    }
}