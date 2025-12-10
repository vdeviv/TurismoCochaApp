package com.turismoapp.mayuandino.feature.calendar.domain.usecase

import com.turismoapp.mayuandino.feature.calendar.domain.model.CalendarEvent
import com.turismoapp.mayuandino.feature.calendar.domain.repository.CalendarEventRepository
import kotlinx.coroutines.flow.Flow

class GetEventsByMonthUseCase(
    private val repository: CalendarEventRepository
) {
    operator fun invoke(year: Int, month: Int): Flow<List<CalendarEvent>> {
        return repository.getEventsByMonth(year, month)
    }
}
