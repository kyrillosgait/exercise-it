package com.kyril.gymondotest.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "exercise")
data class Exercise(
    @PrimaryKey @SerializedName("id") var id: Int,
    @SerializedName("name") var name: String,
    @SerializedName("description") var description: String,
    @SerializedName("category") val categoryId: Int,
    @SerializedName("equipment") val equipmentIds: List<Int>,
    @SerializedName("muscles") val muscleIds: List<Int>,
    @SerializedName("muscles_secondary") val secondaryMuscleIds: List<Int>
//    @ColumnInfo(name = "category") var category: String,
//    @ColumnInfo(name = "equipment) var equipment: String,
//    @ColumnInfo(name = "muscles") var muscles: String,
//    @ColumnInfo(name = "muscles_secondary") var secondaryMuscles: String
)