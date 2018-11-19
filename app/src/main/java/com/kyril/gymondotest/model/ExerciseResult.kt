package com.kyril.gymondotest.model

import androidx.lifecycle.LiveData
import androidx.paging.PagedList

/**
 * ExerciseResult from a getExercisesFromNetwork, which contains LiveData<List<Repo>> holding query data,
 * and a LiveData<String> of network error state.
 */
data class ExerciseResult(
    val data: LiveData<PagedList<Exercise>>,
    val networkErrors: LiveData<String>
)