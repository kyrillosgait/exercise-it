package com.kyril.gymondotest.api

import com.google.gson.annotations.Expose
import com.kyril.gymondotest.model.Category

class CategoryResponse {

    @Expose
    var results: List<Category> = emptyList()
}