package com.csi.palabakosys.util.converters

import androidx.room.TypeConverter
import com.csi.palabakosys.model.EnumDeliveryOption
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object ArrayListConverter {
    @TypeConverter
    fun fromArrayList(value: ArrayList<Int>): String {
        val gson = Gson()
        return gson.toJson(value)
    }

    @TypeConverter
    fun toArrayList(value: String): ArrayList<Int> {
        val listType = object : TypeToken<ArrayList<Int>>() {}.type
        return Gson().fromJson(value, listType)
    }
}