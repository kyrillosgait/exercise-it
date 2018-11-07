package com.kyril.gymondotest.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.kyril.gymondotest.model.Category

@Dao
interface CategoryDao {

    @Insert
    fun insertCategories(categories: List<Category>)

    @Query("SELECT name FROM category WHERE id = :categoryId")
    fun getCategoryById(categoryId: Int): String

    @Query("SELECT COUNT(*) FROM category")
    fun getCategoryRows(): Int

}