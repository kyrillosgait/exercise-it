package com.kyril.gymondotest.db

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kyril.gymondotest.model.Category
import com.kyril.gymondotest.model.Equipment
import com.kyril.gymondotest.model.Exercise
import com.kyril.gymondotest.model.Muscle

@Dao
interface ExerciseDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertExercises(exercises: List<Exercise>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertExercise(exercise: Exercise)

    @Query("SELECT * FROM exercise")
    fun getExercises(): LiveData<List<Exercise>>

    @Query("SELECT COUNT(*) FROM exercise")
    fun getExerciseRows(): Int

    @Query("SELECT * FROM exercise ORDER BY sort_id")
    fun exercises(): DataSource.Factory<Int, Exercise>

    @Query("UPDATE exercise SET thumbnail_url = :thumbnailUrl WHERE id = :exerciseId")
    fun updateExerciseThumbnail(exerciseId: Int, thumbnailUrl: String)

    // Category
    @Insert
    fun insertCategories(categories: List<Category>)

    @Query("SELECT name FROM category WHERE id = :categoryId")
    fun getCategoryById(categoryId: Int): String

    @Query("SELECT COUNT(*) FROM category")
    fun getCategoryRows(): Int


    // Muscles
    @Insert
    fun insertMuscles(muscles: List<Muscle>)

    @Query("SELECT name FROM muscle WHERE id = :muscleId")
    fun getMuscleById(muscleId: Int): String

    @Query("SELECT COUNT(*) FROM muscle")
    fun getMuscleRows(): Int

    // Equipment
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEquipment(categories: List<Equipment>)

    @Query("SELECT name FROM equipment WHERE id = :equipmentId")
    fun getEquipmentById(equipmentId: Int): String

    @Query("SELECT COUNT(*) FROM equipment")
    fun getEquipmentRows(): Int

}