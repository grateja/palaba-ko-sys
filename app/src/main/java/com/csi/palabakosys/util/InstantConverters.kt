package com.csi.palabakosys.util

import androidx.room.TypeConverter
import java.time.Instant

object InstantConverters {
    @TypeConverter
    fun fromInstant(value: Instant?): Long? {
        if(value == null) {
            return null
        }
        return value.toEpochMilli()
    }

    @TypeConverter
    fun toInstant(value: Long?): Instant? {
        if(value == null) {
            return null
        }
        return Instant.ofEpochMilli(value)
    }
}