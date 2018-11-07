package com.kyril.gymondotest.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.kyril.gymondotest.model.Muscle

@Dao
interface MuscleDao {

    @Insert
    fun insertMuscles(muscles: List<Muscle>)

    @Query("SELECT name FROM muscle WHERE id = :muscleId")
    fun getMuscleById(muscleId: Int): String

    @Query("SELECT COUNT(*) FROM muscle")
    fun getMuscleRows(): Int

}