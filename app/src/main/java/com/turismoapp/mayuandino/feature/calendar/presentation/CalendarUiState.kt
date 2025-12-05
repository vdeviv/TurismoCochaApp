package com.turismoapp.mayuandino.feature.calendar.presentation

import com.turismoapp.mayuandino.feature.calendar.domain.model.CalendarEvent
import java.time.LocalDate

data class CalendarUiState(
    val selectedDate: LocalDate = LocalDate.now(),
    val currentMonth: LocalDate = LocalDate.now().withDayOfMonth(1),
    val events: List<CalendarEvent> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
