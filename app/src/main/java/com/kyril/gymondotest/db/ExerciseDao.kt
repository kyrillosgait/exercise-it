package com.kyril.gymondotest.db

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kyril.gymondotest.model.*

@Dao
interface ExerciseDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertExercise(exercise: Exercise)

    @Query("SELECT COUNT(*) FROM exercise")
    fun getExerciseRows(): Int

    @Query("SELECT * FROM exercise WHERE id = :exerciseId")
    fun getExerciseById(exerciseId: Int): Exercise

    @Query("SELECT * FROM exercise")
    fun exercises(): DataSource.Factory<Int, Exercise>

    @Query("UPDATE exercise SET images = :images WHERE id = :exerciseId")
    fun updateExerciseImages(exerciseId: Int, images: List<Image>)

    @Query("UPDATE exercise SET thumbnail_url = :thumbnailUrl WHERE id = :exerciseId")
    fun updateExerciseThumbnail(exerciseId: Int, thumbnailUrl: String)

    // Category
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertCategories(categories: List<Category>)

    @Query("SELECT name FROM category WHERE id = :categoryId")
    fun getCategoryById(categoryId: Int): String

    @Query("SELECT COUNT(*) FROM category")
    fun getCategoryRows(): Int

    // Muscles
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertMuscles(muscles: List<Muscle>)

    @Query("SELECT name FROM muscle WHERE id = :muscleId")
    fun getMuscleById(muscleId: Int): String

    @Query("SELECT COUNT(*) FROM muscle")
    fun getMuscleRows(): Int

    // Equipment
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertEquipment(categories: List<Equipment>)

    @Query("SELECT name FROM equipment WHERE id = :equipmentId")
    fun getEquipmentById(equipmentId: Int): String

    @Query("SELECT COUNT(*) FROM equipment")
    fun getEquipmentRows(): Int

}