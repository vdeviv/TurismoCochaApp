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
    private val insertEvent: InsertCalendarEventUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CalendarUiState())
    val uiState: StateFlow<CalendarUiState> = _uiState

    init {
        loadEventsForDate(_uiState.value.selectedDate)
    }

    fun onDaySelected(date: LocalDate) {
        _uiState.value = _uiState.value.copy(
            selectedDate = date,
            isLoading = true
        )
        loadEventsForDate(date)
    }

    fun addEvent(event: CalendarEvent) {
        viewModelScope.launch {
            insertEvent(event)
            loadEventsForDate(_uiState.value.selectedDate)
        }
    }

    private fun loadEventsForDate(date: LocalDate) {
        viewModelScope.launch {
            getEventsByDate(date).collectLatest { events ->
                _uiState.value = _uiState.value.copy(
                    events = events,
                    isLoading = false,
                    error = null
                )
            }
        }
    }

    fun nextMonth() {
        val newMonth = _uiState.value.currentMonth.plusMonths(1)
        _uiState.value = _uiState.value.copy(currentMonth = newMonth)
    }

    fun previousMonth() {
        val newMonth = _uiState.value.currentMonth.minusMonths(1)
        _uiState.value = _uiState.value.copy(currentMonth = newMonth)
    }
}