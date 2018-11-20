package com.kyril.gymondotest.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "exercise")
data class Exercise(

    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "sort_id") var sortId: Int,
    @Expose @SerializedName("id") val id: Int,
    @Expose @SerializedName("name") val name: String?,
    @Expose @SerializedName("description") val description: String?,
    @Expose @SerializedName("category") val categoryId: Int?,
    @Expose @SerializedName("equipment") val equipmentIds: List<Int>?,
    @Expose @SerializedName("muscles") val muscleIds: List<Int>?,
    @Expose @SerializedName("muscles_secondary") val secondaryMuscleIds: List<Int>?,
    @ColumnInfo(name = "language_id") @Expose @SerializedName("language") val languageId: Int?,
    @ColumnInfo(name = "category") var category: String?,
    @ColumnInfo(name = "equipment") var equipment: String?,
    @ColumnInfo(name = "muscles") var muscles: String?,
    @ColumnInfo(name = "muscles_secondary") var secondaryMuscles: String?,
    @ColumnInfo(name = "images") var images: List<Image>?,
    @ColumnInfo(name = "thumbnail_url") var thumbnailUrl: String?
)