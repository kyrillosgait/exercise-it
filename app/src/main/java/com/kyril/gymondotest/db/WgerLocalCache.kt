package com.kyril.gymondotest.db

import androidx.paging.DataSource
import com.kyril.gymondotest.model.*
import java.util.concurrent.Executor

/**
 * Class that handles the DAO local data source. This ensures that methods are triggered on the
 * correct executor.
 */
class WgerLocalCache(private val exerciseDao: ExerciseDao, private val ioExecutor: Executor) {

    /**
     * Inserts a list of exercises in the database, on a background thread.
     */
    fun insertExercises(exercises: List<Exercise>, insertFinished: () -> Unit) {

        for (exercise in exercises) {

            // Set category
            val category = exercise.categoryId.let { exerciseDao.getCategoryById(it!!) }
            exercise.category = category

            // Set muscleRows
            var muscles = mutableListOf<String>()

            for (muscleId in exercise.muscleIds!!) {
                muscles.add(exerciseDao.getMuscleById(muscleId))
            }

            if (muscles.isNotEmpty()) {
                exercise.muscles = muscles.joinToString(", ")
            }

            // Set muscleRows
            var equipment = mutableListOf<String>()

            for (equipmentId in exercise.equipmentIds!!) {
                equipment.add(exerciseDao.getEquipmentById(equipmentId))
            }

            if (equipment.isNotEmpty()) {
                exercise.equipment = equipment.joinToString(", ")
            }

            ioExecutor.execute {
                exerciseDao.insertExercise(exercise)
            }
        }

        insertFinished()
    }

    fun updateExerciseImages(exerciseId: Int, images: List<Image>) {
        ioExecutor.execute {
            exerciseDao.updateExerciseImages(exerciseId, images)
        }
    }

    /**
     * Updates the thumbnail url field of the given exercise.
     */
    fun updateExerciseThumbnail(exerciseId: Int, thumbnailUrl: String) {
        ioExecutor.execute {
            exerciseDao.updateExerciseThumbnail(exerciseId, thumbnailUrl)
        }
    }

    /**
     * Function to insert categories into the database.
     */
    fun insertCategories(categories: List<Category>) {
        ioExecutor.execute {
            exerciseDao.insertCategories(categories)
        }
    }

    /**
     * Function to insert muscles into the database.
     */
    fun insertMuscles(muscles: List<Muscle>) {
        ioExecutor.execute {
            exerciseDao.insertMuscles(muscles)
        }
    }

    /**
     * Function to insert equipment into the database.
     */
    fun insertEquipment(equipment: List<Equipment>) {
        ioExecutor.execute {
            exerciseDao.insertEquipment(equipment)
        }
    }

    fun exercises(): DataSource.Factory<Int, Exercise> {
        return exerciseDao.exercises()
    }

    fun exerciseRows(): Int {
        return exerciseDao.getExerciseRows()
    }

    fun categoryRows(): Int {
        return exerciseDao.getCategoryRows()
    }

    fun muscleRows(): Int {
        return exerciseDao.getMuscleRows()
    }

    fun equipmentRows(): Int {
        return exerciseDao.getEquipmentRows()
    }
}