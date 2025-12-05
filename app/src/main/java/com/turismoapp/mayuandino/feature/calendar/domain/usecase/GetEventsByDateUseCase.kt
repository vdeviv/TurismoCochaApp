package com.turismoapp.mayuandino.feature.calendar.domain.usecase

import com.turismoapp.mayuandino.feature.calendar.domain.model.CalendarEvent
import com.turismoapp.mayuandino.feature.calendar.domain.repository.CalendarEventRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class GetEventsByDateUseCase(
    private val repository: CalendarEventRepository
) {
    operator fun invoke(date: LocalDate): Flow<List<CalendarEvent>> {
        return repository.getEventsByDate(date)
    }
}
