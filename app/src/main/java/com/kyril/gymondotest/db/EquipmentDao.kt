package com.kyril.gymondotest.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kyril.gymondotest.model.Equipment

@Dao
interface EquipmentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEquipment(categories: List<Equipment>)

    @Query("SELECT name FROM equipment WHERE id = :equipmentId")
    fun getEquipmentById(equipmentId: Int): String

    @Query("SELECT COUNT(*) FROM equipment")
    fun getEquipmentRows(): Int
}