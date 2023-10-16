package com.csi.palabakosys.util.converters

import androidx.room.TypeConverter
import com.csi.palabakosys.model.EnumJoFilterBy

object JoFilterByConverter {
    @TypeConverter
    fun fromJoFilterBy(filterBy: EnumJoFilterBy?): String? {
        return filterBy?.value
    }

    @TypeConverter
    fun joFilterByFromString(value: String?): EnumJoFilterBy? {
        return if(value == null) {
            null
        } else {
            EnumJoFilterBy.fromString(value)
        }
    }
}