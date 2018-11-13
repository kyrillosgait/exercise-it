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

    private val queryLiveData = MutableLiveData<String>()
    val repoResult: LiveData<ExerciseResult> = Transformations.map(queryLiveData) {
        Log.d("MainViewModel", "repoResult")
        repository.getExercises()
    }

    val exercises: LiveData<PagedList<Exercise>> = Transformations.switchMap(repoResult) { it -> it.data }
    val networkErrors: LiveData<String> = Transformations.switchMap(repoResult) { it ->
        it.networkErrors
    }

    fun searchRepo(queryString: String) {
        queryLiveData.postValue(queryString)
    }

    fun getMyExercises(): LiveData<PagedList<Exercise>> {
        loadInitialData()
        val repoResult: LiveData<ExerciseResult> = Transformations.map(queryLiveData) {
            Log.d("MainViewModel", "repoResult")
            repository.getExercises()
        }

        return Transformations.switchMap(repoResult) { it -> it.data }

    }

    private fun loadInitialData() {
        repository.getInitialData()
    }
}