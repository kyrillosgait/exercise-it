package com.kyril.gymondotest.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kyril.gymondotest.model.*

@Database(
    entities = [Category::class, Equipment::class, Exercise::class, Image::class, Muscle::class, Thumbnail::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(IntListTypeConverter::class, ImageListTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun exerciseDao(): ExerciseDao

    companion object {
        private val lock = Any()
        private const val DATABASE_NAME = "Exercises.db"
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            synchronized(lock) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        AppDatabase.DATABASE_NAME
                    )
                        .allowMainThreadQueries()
                        .build()
                }
                return INSTANCE!!
            }
        }
    }
}