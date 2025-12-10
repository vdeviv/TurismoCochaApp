// app/src/main/java/com/turismoapp/mayuandino/feature/calendar/presentation/CalendarScreen.kt
package com.turismoapp.mayuandino.feature.calendar.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.turismoapp.mayuandino.feature.calendar.presentation.components.MiniCalendar
import com.turismoapp.mayuandino.feature.calendar.presentation.components.EventCard
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.*

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

        // ----- Header mes -----
        MonthHeader(
            currentMonth = state.currentMonth,
            onPrevious = { viewModel.previousMonth() },
            onNext = { viewModel.nextMonth() }
        )

        Spacer(modifier = Modifier.height(12.dp))

        // ----- Mini calendario -----
        MiniCalendar(
            currentMonth = state.currentMonth,
            selectedDate = state.selectedDate,
            onDayClick = { viewModel.onDaySelected(it) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Mi calendario",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        when {
            state.isLoading -> {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            state.events.isEmpty() -> {
                Text(
                    "No hay actividades registradas para este día.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            else -> {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(state.events) { event ->
                        EventCard(
                            event = event,
                            onClick = {
                                navController.navigate("event_detail/${event.id}")
                            }
                        )
                    }
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
            "<",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.clickable { onPrevious() }
        )

        Text(
            text = currentMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault())
                .replaceFirstChar { it.titlecase() } + " " + currentMonth.year,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Text(
            ">",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.clickable { onNext() }
        )
    }
}
