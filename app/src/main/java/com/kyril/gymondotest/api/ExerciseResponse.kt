package com.kyril.gymondotest.api

import com.google.gson.annotations.SerializedName
import com.kyril.gymondotest.model.Exercise

/**
 * Data class to hold repo responses from Exercise API calls.
 */
class ExerciseResponse {

    var results: List<Exercise> = emptyList()

    @SerializedName("next")
    var nextPage: String? = null

    @SerializedName("previous")
    var previousPage: String? = null

    var count: Int = 0
}