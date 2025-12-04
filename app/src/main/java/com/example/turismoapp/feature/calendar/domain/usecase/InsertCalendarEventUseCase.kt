package com.example.turismoapp.feature.calendar.domain.usecase

import com.example.turismoapp.feature.calendar.domain.model.CalendarEvent
import com.example.turismoapp.feature.calendar.domain.repository.CalendarEventRepository

class InsertCalendarEventUseCase(
    private val repository: CalendarEventRepository
) {
    suspend operator fun invoke(event: CalendarEvent) {
        repository.insertEvent(event)
    }
}
