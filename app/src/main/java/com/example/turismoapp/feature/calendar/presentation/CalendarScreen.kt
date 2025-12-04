package com.example.turismoapp.feature.calendar.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun CalendarScreen(
    navController: NavController,
    viewModel: CalendarViewModel
) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        MonthHeader(
            currentMonth = state.currentMonth,
            onPrevious = { viewModel.previousMonth() },
            onNext = { viewModel.nextMonth() }
        )

        Spacer(modifier = Modifier.height(8.dp))

        CalendarGrid(
            month = state.currentMonth,
            selectedDate = state.selectedDate,
            onDayClick = { date -> viewModel.onDaySelected(date) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Actividades para ${state.selectedDate.dayOfMonth} " +
                    state.selectedDate.month.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (state.isLoading) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (state.events.isEmpty()) {
            Text(
                text = "No hay actividades registradas para este día.",
                style = MaterialTheme.typography.bodyMedium
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(state.events) { event ->
                    EventCard(eventTitle = event.title,
                        location = event.location,
                        price = event.price,
                        description = event.description)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
private fun MonthHeader(
    currentMonth: LocalDate,
    onPrevious: () -> Unit,
    onNext: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "<",
            modifier = Modifier
                .clickable { onPrevious() }
                .padding(8.dp),
            style = MaterialTheme.typography.titleLarge
        )

        Text(
            text = currentMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault())
                    + " " + currentMonth.year,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = ">",
            modifier = Modifier
                .clickable { onNext() }
                .padding(8.dp),
            style = MaterialTheme.typography.titleLarge
        )
    }
}

@Composable
private fun CalendarGrid(
    month: LocalDate,
    selectedDate: LocalDate,
    onDayClick: (LocalDate) -> Unit
) {
    Column {
        // Cabecera de días
        val daysOfWeek = listOf("L", "M", "X", "J", "V", "S", "D")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            daysOfWeek.forEach {
                Text(
                    text = it,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        val firstDayOfMonth = month
        val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value // 1 = Lunes
        val daysInMonth = month.lengthOfMonth()

        var currentDay = 1
        var row = 0

        while (currentDay <= daysInMonth) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                for (col in 1..7) {
                    val index = row * 7 + col
                    if (index < firstDayOfWeek || currentDay > daysInMonth) {
                        Box(modifier = Modifier.size(32.dp)) { }
                    } else {
                        val date = month.withDayOfMonth(currentDay)
                        val isSelected = date == selectedDate

                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clickable { onDayClick(date) }
                                .background(
                                    if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                                    else MaterialTheme.colorScheme.background
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = currentDay.toString(),
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                        currentDay++
                    }
                }
            }
            row++
        }
    }
}

@Composable
private fun EventCard(
    eventTitle: String,
    location: String?,
    price: Double,
    description: String?
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = eventTitle,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            if (!location.isNullOrBlank()) {
                Text(
                    text = location,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "$ ${String.format("%.2f", price)}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )

            if (!description.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
