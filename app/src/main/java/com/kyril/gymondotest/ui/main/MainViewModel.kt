package com.kyril.gymondotest.ui.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.kyril.gymondotest.api.*
import com.kyril.gymondotest.db.AppDatabase
import com.kyril.gymondotest.model.Exercise
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class MainViewModel(application: Application) : AndroidViewModel(application) {

    val myExecutor: Executor = Executors.newSingleThreadExecutor()
    val database: AppDatabase = AppDatabase.getInstance(getApplication())

    fun getExercises(): LiveData<List<Exercise>> {

        if (database.categoryDao().getCategoryRows() == 0) {
            loadCategories()
        }

        if (database.muscleDao().getMuscleRows() == 0) {
            loadMuscles()
        }

        if (database.equipmentDao().getEquipmentRows() == 0) {
            loadEquipment()
        }

        loadExercises()

        return database.exerciseDao().getExercises()
    }

    private fun loadExercises() {
        Log.d("ViewModel", "Starting Exercise API Call...")

        WgerService.create()
            .getExercises(1)
            .enqueue(object : Callback<ExerciseResponse> {

                override fun onResponse(call: Call<ExerciseResponse>, response: Response<ExerciseResponse>) {

                    Log.d("ViewModel - Success", "Inserting exercises...")

                    val results = response.body()?.results

                    for (exercise in results!!) {

                        // Set category
                        val category = database.categoryDao().getCategoryById(exercise.categoryId)
                        exercise.category = category

                        // Set muscles
//                        if (exercise.muscleIds.isEmpty()) {
//                            exercise.muscles = null
//                        }

                        var muscles = mutableListOf<String>()

                        for (muscleId in exercise.muscleIds) {
                            muscles.add(database.muscleDao().getMuscleById(muscleId))
                        }

                        if (muscles.isNotEmpty()) {
                            exercise.muscles = muscles.joinToString(", ")
                        }

                        // Set muscles
                        var equipment = mutableListOf<String>()

                        for (equipmentId in exercise.equipmentIds) {
                            equipment.add(database.equipmentDao().getEquipmentById(equipmentId))
                        }

                        if (equipment.isNotEmpty()) {
                            exercise.equipment = equipment.joinToString(", ")
                        }

                        // Insert exercise in database
                        myExecutor.execute { database.exerciseDao().insertExercise(exercise) }
                    }
                }

                override fun onFailure(call: Call<ExerciseResponse>, t: Throwable) {
                    Log.d("ViewModel - Error", t.toString())
                }

            })
    }

    private fun loadCategories() {
        Log.d("ViewModel", "Starting Categories API Call...")

        WgerService.create()
            .getAllCategories()
            .enqueue(object : Callback<CategoryResponse> {

                override fun onResponse(call: Call<CategoryResponse>, response: Response<CategoryResponse>) {
                    Log.d("ViewModel - Success", "Getting categories...")
                    val results = response.body()?.results

                    myExecutor.execute { (database.categoryDao().insertCategories(results!!)) }
                }

                override fun onFailure(call: Call<CategoryResponse>, t: Throwable) {
                    Log.d("ViewModel - Error", t.toString())
                }

            })
    }

    private fun loadMuscles() {
        Log.d("ViewModel", "Starting Muscles API Call...")

        WgerService.create()
            .getAllMuscles()
            .enqueue(object : Callback<MuscleResponse> {

                override fun onResponse(call: Call<MuscleResponse>, response: Response<MuscleResponse>) {
                    Log.d("ViewModel - Success", "Getting muscles...")
                    val results = response.body()?.results

                    myExecutor.execute { (database.muscleDao().insertMuscles(results!!)) }
                }

                override fun onFailure(call: Call<MuscleResponse>, t: Throwable) {
                    Log.d("ViewModel - Error", t.toString())
                }

            })
    }

    private fun loadEquipment() {
        Log.d("ViewModel", "Starting Equipment API Call...")

        WgerService.create()
            .getAllEquipment()
            .enqueue(object : Callback<EquipmentResponse> {

                override fun onResponse(call: Call<EquipmentResponse>, response: Response<EquipmentResponse>) {
                    Log.d("ViewModel - Success", "Getting equipment...")
                    val results = response.body()?.results

                    myExecutor.execute { (database.equipmentDao().insertEquipment(results!!)) }
                }

                override fun onFailure(call: Call<EquipmentResponse>, t: Throwable) {
                    Log.d("ViewModel - Error", t.toString())
                }

            })
    }

}