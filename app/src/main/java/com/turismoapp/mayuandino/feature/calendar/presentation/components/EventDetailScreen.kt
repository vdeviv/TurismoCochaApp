package com.turismoapp.mayuandino.feature.calendar.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.turismoapp.mayuandino.feature.calendar.presentation.CalendarViewModel

@Composable
fun EventDetailScreen(
    eventId: Long,
    viewModel: CalendarViewModel
) {
    val state by viewModel.uiState.collectAsState()
    val event = state.events.find { it.id == eventId }

    if (event == null) {
        Text(
            text = "Evento no encontrado",
            modifier = Modifier.padding(20.dp)
        )
        return
    }

    Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {

        AsyncImage(
            model = event.imageUrl,
            contentDescription = event.title,
            modifier = Modifier.fillMaxWidth().height(220.dp),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(event.title, style = MaterialTheme.typography.headlineSmall)
        Text(event.location ?: "", style = MaterialTheme.typography.bodyMedium)
        Text("Precio: Bs. ${event.price}", style = MaterialTheme.typography.bodyMedium)

        Spacer(modifier = Modifier.height(16.dp))

        Text(event.description ?: "", style = MaterialTheme.typography.bodyLarge)
    }
}