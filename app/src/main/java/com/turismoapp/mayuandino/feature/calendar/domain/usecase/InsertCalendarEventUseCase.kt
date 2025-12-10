package com.turismoapp.mayuandino.feature.calendar.domain.usecase

import com.turismoapp.mayuandino.feature.calendar.domain.model.CalendarEvent
import com.turismoapp.mayuandino.feature.calendar.domain.repository.CalendarEventRepository

class InsertCalendarEventUseCase(
    private val repository: CalendarEventRepository
) {
    suspend operator fun invoke(event: CalendarEvent) {
        repository.insertEvent(event)
    }
}
