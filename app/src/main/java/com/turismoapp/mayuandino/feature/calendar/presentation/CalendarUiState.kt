package com.turismoapp.mayuandino.feature.calendar.presentation

import com.turismoapp.mayuandino.feature.calendar.domain.model.CalendarEvent
import java.time.LocalDate

data class CalendarUiState(
    val currentMonth: LocalDate = LocalDate.now().withDayOfMonth(1),
    val selectedDate: LocalDate = LocalDate.now(),
    val events: List<CalendarEvent> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val eventsOfMonth: List<CalendarEvent> = emptyList()
)