package com.kyril.gymondotest.api

import com.google.gson.annotations.Expose
import com.kyril.gymondotest.model.Image

class ImageResponse {

    @Expose
    var results: List<Image> = emptyList()
}