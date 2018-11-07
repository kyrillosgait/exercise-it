package com.kyril.gymondotest.api

import com.google.gson.annotations.Expose
import com.kyril.gymondotest.model.Equipment

class EquipmentResponse {

    @Expose
    var results: List<Equipment> = emptyList()
}