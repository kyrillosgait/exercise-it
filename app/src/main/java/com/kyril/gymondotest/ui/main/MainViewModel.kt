package com.kyril.gymondotest.ui.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kyril.gymondotest.api.ExerciseResponse
import com.kyril.gymondotest.api.WgerService
import com.kyril.gymondotest.db.AppDatabase
import com.kyril.gymondotest.model.Exercise
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(application: Application) : AndroidViewModel(application) {

    var exerciseList: MutableLiveData<List<Exercise>> = MutableLiveData()

    fun getExercises(): LiveData<List<Exercise>> {
        loadExercises()
        return exerciseList
    }

    private fun loadExercises() {
        Log.d("ViewModel", "Starting Exercise API Call...")

        WgerService.create()
            .getExercises(1)
            .enqueue(object : Callback<ExerciseResponse> {

                override fun onResponse(call: Call<ExerciseResponse>, response: Response<ExerciseResponse>) {
                    Log.d("ViewModel - Success", "Setting exerciseList data...")
                    val results = response.body()?.results

                    Thread {
                        AppDatabase.getInstance(getApplication()).exerciseDao().insertExercises(results!!)
                    }.start()

                    exerciseList.value = results
                }

                override fun onFailure(call: Call<ExerciseResponse>, t: Throwable) {
                    Log.d("ViewModel - Error", t.toString())
                }

            })
    }

}