package com.kyril.gymondotest.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.kyril.gymondotest.model.Muscle

class MuscleResponse {

    @Expose
    @SerializedName("results")
    var muscles: List<Muscle> = emptyList()
}