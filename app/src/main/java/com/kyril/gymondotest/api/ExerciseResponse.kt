package com.kyril.gymondotest.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.kyril.gymondotest.model.Exercise

/**
 * Data class to hold repo responses from Exercise API calls.
 */
class ExerciseResponse {

    @Expose
    @SerializedName("results")
    var exercises: List<Exercise> = emptyList()

}