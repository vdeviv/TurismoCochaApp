package com.example.turismoapp.feature.calendar.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.turismoapp.feature.calendar.domain.usecase.GetEventsByDateUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDate

class CalendarViewModel(
    private val getEventsByDate: GetEventsByDateUseCase
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
