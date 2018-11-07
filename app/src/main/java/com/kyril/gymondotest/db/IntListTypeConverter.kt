package com.kyril.gymondotest.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class IntListTypeConverter {

    @TypeConverter
    fun fromJson(value: String): List<Int> {
        val listType = object : TypeToken<List<Int>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun toJson(list: List<Int>): String {
        val gson = Gson()
        return gson.toJson(list)
    }
}