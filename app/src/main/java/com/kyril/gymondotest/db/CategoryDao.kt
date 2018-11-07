package com.kyril.gymondotest.db

import androidx.room.Dao
import androidx.room.Insert
import com.kyril.gymondotest.model.Category

@Dao
interface CategoryDao {

    @Insert
    fun insertCategories(categories: List<Category>)

}