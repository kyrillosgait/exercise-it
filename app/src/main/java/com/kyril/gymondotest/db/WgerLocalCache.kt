package com.kyril.gymondotest.db

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

    fun insertCategories(categories: List<Category>, insertFinished: () -> Unit) {
        ioExecutor.execute {
            exerciseDao.insertCategories(categories)
        }
        insertFinished()
    }

    fun insertMuscles(muscles: List<Muscle>, insertFinished: () -> Unit) {
        ioExecutor.execute {
            exerciseDao.insertMuscles(muscles)
        }
        insertFinished()
    }

    fun insertEquipment(equipment: List<Equipment>, insertFinished: () -> Unit) {
        ioExecutor.execute {
            exerciseDao.insertEquipment(equipment)
        }
        insertFinished()
    }

    fun updateExercise(exerciseId: Int, thumbnailUrl: String) {
        ioExecutor.execute {
            exerciseDao.updateExerciseThumbnail(exerciseId, thumbnailUrl)
        }
    }

    /**
     * Insert a list of exercises in the database, on a background thread.
     */
    fun insertExercises(exercises: List<Exercise>, insertFinished: () -> Unit) {

        for (exercise in exercises) {

            // Set category
            val category = exerciseDao.getCategoryById(exercise.categoryId)
            exercise.category = category

            // Set muscles
            var muscles = mutableListOf<String>()

            for (muscleId in exercise.muscleIds!!) {
                muscles.add(exerciseDao.getMuscleById(muscleId))
            }

            if (muscles.isNotEmpty()) {
                exercise.muscles = muscles.joinToString(", ")
            }

            // Set muscles
            var equipment = mutableListOf<String>()

            for (equipmentId in exercise.equipmentIds!!) {
                equipment.add(exerciseDao.getEquipmentById(equipmentId))
            }

            if (equipment.isNotEmpty()) {
                exercise.equipment = equipment.joinToString(", ")
            }

            // Insert exercise in database
            ioExecutor.execute {
                exerciseDao.insertExercise(exercise)
            }
        }

        insertFinished()
    }

    fun exercises(): DataSource.Factory<Int, Exercise> {
        return exerciseDao.exercises()
    }

    fun exerciseRows(): Int {
        return exerciseDao.getExerciseRows()
    }

    fun categories(): Int {
        return exerciseDao.getCategoryRows()
    }

    fun muscles(): Int {
        return exerciseDao.getMuscleRows()
    }

    fun equipment(): Int {
        return exerciseDao.getEquipmentRows()
    }

}