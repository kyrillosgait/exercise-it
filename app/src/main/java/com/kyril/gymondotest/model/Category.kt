package com.kyril.gymondotest.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose

@Entity(tableName = "category")
data class Category(
    @PrimaryKey @Expose val id: Int,
    @Expose val name: String
)