package com.kyril.gymondotest.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "image")
data class Image(
    @PrimaryKey @Expose val id: Int?,
    @ColumnInfo(name = "image_url") @Expose @SerializedName("image") val imageUrl: String?,
    @ColumnInfo(name = "exercise_id") @Expose @SerializedName("exercise") val exerciseId: Int?
)