package com.turismoapp.mayuandino.feature.calendar.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.turismoapp.mayuandino.feature.calendar.domain.model.CalendarEvent
import com.turismoapp.mayuandino.feature.calendar.domain.usecase.GetEventsByDateUseCase
import com.turismoapp.mayuandino.feature.calendar.domain.usecase.GetEventsByMonthUseCase
import com.turismoapp.mayuandino.feature.calendar.domain.usecase.InsertCalendarEventUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDate

class CalendarViewModel(
    private val getEventsByDate: GetEventsByDateUseCase,
    private val getEventsByMonth: GetEventsByMonthUseCase,
    private val insertEventUseCase: InsertCalendarEventUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CalendarUiState())
    val uiState: StateFlow<CalendarUiState> = _uiState

    init {
        val month = _uiState.value.currentMonth
        loadMonth(month.year, month.monthValue)
        loadEventsForDate(_uiState.value.selectedDate)
    }

    // SELECCIÓN DE DÍA
    fun onDaySelected(date: LocalDate) {
        _uiState.value = _uiState.value.copy(selectedDate = date)
        loadEventsForDate(date)
    }

    // CAMBIO DE MES SIGUIENTE
    fun nextMonth() {
        val newMonth = _uiState.value.currentMonth.plusMonths(1)
        _uiState.value = _uiState.value.copy(currentMonth = newMonth)
        loadMonth(newMonth.year, newMonth.monthValue)
    }
    // CAMBIO DE MES ANTERIOR
    fun previousMonth() {
        val newMonth = _uiState.value.currentMonth.minusMonths(1)
        _uiState.value = _uiState.value.copy(currentMonth = newMonth)
        loadMonth(newMonth.year, newMonth.monthValue)
    }

    // CARGAR LOS EVENTOS DEL DÍA SELECCIONADO

    private fun loadEventsForDate(date: LocalDate) {
        viewModelScope.launch {
            getEventsByDate(date).collectLatest { events ->
                _uiState.value = _uiState.value.copy(events = events)
            }
        }
    }

    // CARGAR TODOS LOS EVENTOS DEL MES (PARA MOSTRAR PUNTITOS)
    private fun loadMonth(year: Int, month: Int) {
        viewModelScope.launch {
            getEventsByMonth(year, month).collectLatest { events ->

                val eventDaysSet = events.map { it.date }.toSet()

                _uiState.value = _uiState.value.copy(
                    eventsOfMonth = events,
                    eventDays = eventDaysSet
                )
            }
        }
    }

    fun getEventById(id: String): CalendarEvent? {
        return uiState.value.eventsOfMonth.find { it.id == id }
            ?: uiState.value.events.find { it.id == id }
    }
}
