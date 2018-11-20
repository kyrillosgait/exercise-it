package com.kyril.gymondotest.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kyril.gymondotest.model.Image

class ImageListTypeConverter {

    @TypeConverter
    fun toImagesList(value: String): List<Image>? {
        val listType = object : TypeToken<List<Image>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun toImagesJson(list: List<Image>?): String {
        return Gson().toJson(list)
    }
}