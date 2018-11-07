package com.kyril.gymondotest.db

import androidx.room.Dao
import androidx.room.Insert
import com.kyril.gymondotest.model.Image

@Dao
interface ImageDao {

    @Insert
    fun insertImages(exercises: List<Image>)

}