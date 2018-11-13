package com.kyril.gymondotest.api

import com.google.gson.annotations.Expose
import com.kyril.gymondotest.model.Thumbnail

class ThumbnailResponse {

    @Expose
    var thumbnail: Thumbnail? = null
}