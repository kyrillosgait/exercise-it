package com.kyril.gymondotest.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.kyril.gymondotest.model.Image

class ImageResponse {

    @Expose
    @SerializedName("results")
    var images: List<Image> = emptyList()
}