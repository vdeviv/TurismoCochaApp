package com.turismoapp.mayuandino.feature.calendar.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.foundation.clickable

import com.turismoapp.mayuandino.feature.calendar.presentation.components.EventCard
import com.turismoapp.mayuandino.feature.calendar.presentation.components.MiniCalendar
import java.time.format.DateTimeFormatter

@Composable
fun CalendarScreen(
    navController: NavController,
    viewModel: CalendarViewModel
) {
    val uiState = viewModel.uiState.collectAsState().value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        // ------ HEADER DEL MES ------
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "<",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.clickable { viewModel.previousMonth() }
            )

            Text(
                uiState.currentMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy")),
                style = MaterialTheme.typography.titleLarge
            )

            Text(
                ">",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.clickable { viewModel.nextMonth() }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // ------ MINI CALENDARIO ------
        MiniCalendar(
            selectedDate = uiState.selectedDate,
            currentMonth = uiState.currentMonth,
            onDayClick = { viewModel.onDaySelected(it) }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Mi calendario",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(12.dp))

        // ------ EVENTOS DEL DÍA ------
        if (uiState.events.isEmpty()) {
            Text("No hay actividades registradas para este día.")
        } else {
            LazyColumn {
                items(uiState.events) { event ->
                    EventCard(
                        event = event,
                        onClick = {
                            navController.navigate("event_detail/${event.id}")
                        }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}
