package com.example.turismoapp.feature.calendar.domain.repository

import com.example.turismoapp.feature.calendar.domain.model.CalendarEvent
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface CalendarEventRepository {
    fun getEventsByDate(date: LocalDate): Flow<List<CalendarEvent>>
    fun getEventsByMonth(year: Int, month: Int): Flow<List<CalendarEvent>>
    suspend fun insertEvent(event: CalendarEvent)
}