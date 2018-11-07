package com.kyril.gymondotest.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.kyril.gymondotest.model.Exercise

@Dao
interface ExerciseDao {

    @Insert
    fun insertExercises(exercises: List<Exercise>)

    @Query("SELECT * FROM exercise")
    fun getExercises(): LiveData<List<Exercise>>

}