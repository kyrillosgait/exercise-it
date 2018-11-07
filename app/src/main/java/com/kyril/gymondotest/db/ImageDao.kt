package com.kyril.gymondotest.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.kyril.gymondotest.model.Image

@Dao
interface ImageDao {

    @Insert
    fun insertImages(exercises: List<Image>)

    @Query("SELECT image_url FROM image WHERE exercise_id = :exerciseId GROUP BY exercise_id")
    fun getImageById(exerciseId: Int): List<String>

    @Query("SELECT COUNT(*) FROM image")
    fun getImageRows(): Int

}