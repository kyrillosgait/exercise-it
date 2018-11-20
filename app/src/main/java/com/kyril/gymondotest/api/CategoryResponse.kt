package com.kyril.gymondotest.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.kyril.gymondotest.model.Category

class CategoryResponse {

    @Expose
    @SerializedName("results")
    var categories: List<Category> = emptyList()
}