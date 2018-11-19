package com.kyril.gymondotest.db

import android.util.Log
import androidx.paging.DataSource
import com.kyril.gymondotest.model.Category
import com.kyril.gymondotest.model.Equipment
import com.kyril.gymondotest.model.Exercise
import com.kyril.gymondotest.model.Muscle
import java.util.concurrent.Executor

/**
 * Class that handles the DAO local data source. This ensures that methods are triggered on the
 * correct executor.
 */
class WgerLocalCache(
    private val exerciseDao: ExerciseDao,
    private val ioExecutor: Executor
) {

    /**
     * Inserts a list of exercises in the database, on a background thread.
     */
    fun insertExercises(exercises: List<Exercise>?, insertFinished: () -> Unit) {

        if (exercises != null) {
            for (exercise in exercises) {
                Log.d("CACHE_EXERCISE", exercise.toString())

                // Set category
                val category = exerciseDao.getCategoryById(exercise.categoryId)
                exercise.category = category

                // Set muscleRows
                var muscles = mutableListOf<String>()

                if (exercise.muscleIds != null) {
                    for (muscleId in exercise.muscleIds) {
                        muscles.add(exerciseDao.getMuscleById(muscleId))
                    }
                }

                if (muscles.isNotEmpty()) {
                    exercise.muscles = muscles.joinToString(", ")
                }

                // Set muscleRows
                var equipment = mutableListOf<String>()

                if (exercise.equipmentIds != null) {
                    for (equipmentId in exercise.equipmentIds!!) {
                        equipment.add(exerciseDao.getEquipmentById(equipmentId))
                    }
                }


                if (equipment.isNotEmpty()) {
                    exercise.equipment = equipment.joinToString(", ")
                }

                // Insert exercise in database
                ioExecutor.execute {
                    exerciseDao.insertExercise(exercise)
                }
            }
        }

        insertFinished()
    }

    /**
     * Updates the thumbnail url field of the given exercise.
     */
    fun updateExercise(exerciseId: Int, thumbnailUrl: String) {
        ioExecutor.execute {
            exerciseDao.updateExerciseThumbnail(exerciseId, thumbnailUrl)
        }
    }

    /**
     * Function to insert categories into the database.
     */
    fun insertCategories(categories: List<Category>, insertFinished: () -> Unit) {
        ioExecutor.execute {
            exerciseDao.insertCategories(categories)
        }
        insertFinished()
    }

    /**
     * Function to insert muscles into the database.
     */
    fun insertMuscles(muscles: List<Muscle>, insertFinished: () -> Unit) {
        ioExecutor.execute {
            exerciseDao.insertMuscles(muscles)
        }
        insertFinished()
    }

    /**
     * Function to insert equipment into the database.
     */
    fun insertEquipment(equipment: List<Equipment>, insertFinished: () -> Unit) {
        ioExecutor.execute {
            exerciseDao.insertEquipment(equipment)
        }
        insertFinished()
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