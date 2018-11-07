package com.kyril.gymondotest.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "muscle")
data class Muscle(
    @PrimaryKey val id: Int,
    val name: String,
    @ColumnInfo(name = "is_front") @SerializedName("is_front") val isFront: Boolean
)