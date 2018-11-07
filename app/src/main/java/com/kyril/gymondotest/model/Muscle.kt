package com.kyril.gymondotest.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "muscle")
data class Muscle(

    @PrimaryKey @Expose val id: Int,
    @Expose val name: String,
    @ColumnInfo(name = "is_front") @Expose @SerializedName("is_front") val isFront: Boolean
)