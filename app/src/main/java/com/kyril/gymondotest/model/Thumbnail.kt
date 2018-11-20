package com.kyril.gymondotest.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose

@Entity(tableName = "thumbnail")
data class Thumbnail(

    @PrimaryKey(autoGenerate = true) var id: Int,
    @Expose val url: String

)