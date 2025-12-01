package com.example.turismoapp.framework.local.db

import android.content.Context
import androidx.room.Room
import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.turismoapp.framework.local.dao.CalendarEventDao
import com.example.turismoapp.framework.local.dao.DestinationDao
import com.example.turismoapp.framework.local.entity.CalendarEventEntity
import com.example.turismoapp.framework.local.entity.DestinationEntity

@Database(
    entities = [
        DestinationEntity::class,
        CalendarEventEntity::class
    ],
    version = 1,
    exportSchema = false
)

abstract class AppDatabase : RoomDatabase() {

    abstract fun destinationDao(): DestinationDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "turismo.db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}