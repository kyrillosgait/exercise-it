package com.kyril.gymondotest.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.kyril.gymondotest.model.Equipment

class EquipmentResponse {

    @Expose @SerializedName("results")
    var equipment: List<Equipment> = emptyList()
}