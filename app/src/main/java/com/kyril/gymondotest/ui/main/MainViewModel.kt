package com.kyril.gymondotest.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.kyril.gymondotest.data.WgerRepository
import com.kyril.gymondotest.model.Exercise
import com.kyril.gymondotest.model.ExerciseResult

class MainViewModel(private val repository: WgerRepository) : ViewModel() {

    private fun loadInitialData() {
        repository.getInitialData()
    }

    private val queryLiveData = MutableLiveData<String>()
    val exerciseResult: LiveData<ExerciseResult> = Transformations.map(queryLiveData) { repository.getExercises() }
    val exercises: LiveData<PagedList<Exercise>> = Transformations.switchMap(exerciseResult) { it -> it.data }
    val networkErrors: LiveData<String> = Transformations.switchMap(exerciseResult) { it -> it.networkErrors }

    fun init(string: String) {
        loadInitialData()
        queryLiveData.postValue(string)
    }

    fun getMyExercises(): LiveData<PagedList<Exercise>> {

        val repoResult: LiveData<ExerciseResult> = Transformations.map(queryLiveData) {
            Log.d("MainViewModel", "exerciseResult")
            repository.getExercises()
        }

        return Transformations.switchMap(repoResult) { it -> it.data }
    }

}