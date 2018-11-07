package com.kyril.gymondotest.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "exercise")
data class Exercise(

    @PrimaryKey @Expose @SerializedName("id") var id: Int,
    @Expose @SerializedName("name") var name: String,
    @Expose @SerializedName("description") var description: String = "",
    @ColumnInfo(name = "category_id") @Expose @SerializedName("category") val categoryId: Int,
    @Expose @SerializedName("equipment") val equipmentIds: List<Int>,
    @Expose @SerializedName("muscles") val muscleIds: List<Int>,
    @Expose @SerializedName("muscles_secondary") val secondaryMuscleIds: List<Int>,
    @ColumnInfo(name = "category") var category: String?,
    @ColumnInfo(name = "equipment") var equipment: String?,
    @ColumnInfo(name = "muscles") var muscles: String?,
    @ColumnInfo(name = "muscles_secondary") var secondaryMuscles: String?
)