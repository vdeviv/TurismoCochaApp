package com.turismoapp.mayuandino.feature.calendar.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.turismoapp.mayuandino.feature.calendar.domain.model.CalendarEvent
import com.turismoapp.mayuandino.feature.calendar.domain.usecase.GetEventsByDateUseCase
import com.turismoapp.mayuandino.feature.calendar.domain.usecase.InsertCalendarEventUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDate

class CalendarViewModel(
    private val getEventsByDate: GetEventsByDateUseCase,
    private val insertEventUseCase: InsertCalendarEventUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CalendarUiState())
    val uiState: StateFlow<CalendarUiState> = _uiState

    init {
        loadEventsForDate(_uiState.value.selectedDate)
    }

    fun onDaySelected(date: LocalDate) {
        _uiState.value = _uiState.value.copy(selectedDate = date)
        loadEventsForDate(date)
    }

    private fun loadEventsForDate(date: LocalDate) {
        viewModelScope.launch {
            getEventsByDate(date).collectLatest { events ->
                _uiState.value = _uiState.value.copy(events = events)
            }
        }
    }

    fun nextMonth() {
        val newMonth = _uiState.value.currentMonth.plusMonths(1)
        _uiState.value = _uiState.value.copy(currentMonth = newMonth)

        // Mantener el día seleccionado dentro del nuevo mes
        val selected = _uiState.value.selectedDate.withMonth(newMonth.monthValue)
        val safeDate = selected.withDayOfMonth(minOf(selected.dayOfMonth, newMonth.lengthOfMonth()))

        _uiState.value = _uiState.value.copy(selectedDate = safeDate)

        loadEventsForDate(safeDate)
    }

    fun previousMonth() {
        val newMonth = _uiState.value.currentMonth.minusMonths(1)
        _uiState.value = _uiState.value.copy(currentMonth = newMonth)

        val selected = _uiState.value.selectedDate.withMonth(newMonth.monthValue)
        val safeDate = selected.withDayOfMonth(minOf(selected.dayOfMonth, newMonth.lengthOfMonth()))

        _uiState.value = _uiState.value.copy(selectedDate = safeDate)

        loadEventsForDate(safeDate)
    }
}
