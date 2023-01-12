package com.csi.palabakosys.util

import androidx.room.TypeConverter
import com.csi.palabakosys.model.MachineType

object MachineTypeConverter {
    @TypeConverter
    fun fromMachineType(machineType: MachineType?): String? {
        return machineType?.toString()
    }

    @TypeConverter
    fun machineTypeFromString(string: String?): MachineType? {
        return if(string == null) {
            null
        } else {
            MachineType.fromString(string)
        }
    }
}