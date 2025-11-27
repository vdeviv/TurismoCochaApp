package com.example.turismoapp.Framework.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.turismoapp.Framework.local.dao.CalendarEventDao
import com.example.turismoapp.Framework.local.dao.DestinationDao
import com.example.turismoapp.Framework.local.entity.CalendarEventEntity
import com.example.turismoapp.Framework.local.entity.DestinationEntity

@Database(
    entities = [
        DestinationEntity::class,
        CalendarEventEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun destinationDao(): DestinationDao
    abstract fun calendarEventDao(): CalendarEventDao
}
