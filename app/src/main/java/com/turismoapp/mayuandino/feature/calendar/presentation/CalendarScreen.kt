package com.turismoapp.mayuandino.feature.calendar.presentation

import androidx.compose.foundation.clickable
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

import com.turismoapp.mayuandino.feature.calendar.presentation.components.EventCard
import com.turismoapp.mayuandino.feature.calendar.presentation.components.MiniCalendar

// ---------------- IMPORTS DE COLORES ----------------
import com.turismoapp.mayuandino.ui.theme.PurpleMayu
import com.turismoapp.mayuandino.ui.theme.GrayText

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
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {

        // -------------------- HEADER --------------------
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "<",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.clickable { viewModel.previousMonth() }
            )

            Text(
                uiState.currentMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy")),
                style = MaterialTheme.typography.headlineMedium
            )

            Text(
                ">",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.clickable { viewModel.nextMonth() }
            )
        }

        Spacer(Modifier.height(16.dp))

        // -------------------- MINI CALENDARIO --------------------
        MiniCalendar(
            currentMonth = uiState.currentMonth,
            selectedDate = uiState.selectedDate,
            eventDays = uiState.eventDays,
            onDayClick = { viewModel.onDaySelected(it) }
        )

        Spacer(Modifier.height(32.dp))

        // -------------------- TÍTULO "MI CALENDARIO" --------------------
        Text(
            "Mi calendario",
            style = MaterialTheme.typography.headlineSmall,
            color = PurpleMayu
        )

        Spacer(Modifier.height(16.dp))

        // -------------------- EVENTOS --------------------
        if (uiState.events.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "No hay actividades para esta fecha.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = GrayText
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(uiState.events) { event ->
                    EventCard(
                        event = event,
                        onClick = {
                            navController.navigate("eventDetail/${event.id}")
                        }
                    )
                }
            }
        }
    }
}