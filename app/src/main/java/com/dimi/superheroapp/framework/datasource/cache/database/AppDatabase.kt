package com.dimi.superheroapp.framework.datasource.cache.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dimi.superheroapp.framework.datasource.cache.model.SuperHeroCacheEntity

@Database(
    entities = [
        SuperHeroCacheEntity::class
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun mainDao(): MainDao

    companion object {
        const val DATABASE_NAME: String = "app_db"
    }
}