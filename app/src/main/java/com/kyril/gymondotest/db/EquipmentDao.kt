package com.kyril.gymondotest.db

import androidx.room.Dao
import androidx.room.Insert
import com.kyril.gymondotest.model.Equipment

@Dao
interface EquipmentDao {

    @Insert
    fun insertEquipment(categories: List<Equipment>)

}