package com.example.turismoapp.feature.calendar.domain.model

import java.time.LocalDate

data class CalendarEvent(
    val id: Long = 0L,
    val title: String,
    val location: String?,
    val price: Double,
    val date: LocalDate,
    val description: String? = null
)