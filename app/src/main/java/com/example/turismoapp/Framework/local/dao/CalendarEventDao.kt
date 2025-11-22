package com.example.turismoapp.Framework.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.turismoapp.Framework.local.entity.CalendarEventEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CalendarEventDao {

    @Query("SELECT * FROM calendar_events WHERE date = :date ORDER BY price DESC")
    fun getEventsByDate(date: String): Flow<List<CalendarEventEntity>>

    @Query("SELECT * FROM calendar_events WHERE date LIKE :monthPrefix || '%'")
    fun getEventsByMonth(monthPrefix: String): Flow<List<CalendarEventEntity>>
    // monthPrefix = "2025-11"

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: CalendarEventEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvents(events: List<CalendarEventEntity>)

    @Query("DELETE FROM calendar_events")
    suspend fun clear()
}
