package com.kyril.gymondotest.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.kyril.gymondotest.model.Thumbnail

class ThumbnailResponse {

    @Expose @SerializedName("small")
    var thumbnail: Thumbnail? = null
}