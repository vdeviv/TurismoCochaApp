package com.turismoapp.mayuandino.feature.calendar.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.turismoapp.mayuandino.feature.calendar.presentation.CalendarViewModel

@Composable
fun EventDetailScreen(
    eventId: String,        // ← CAMBIO IMPORTANTE
    viewModel: CalendarViewModel
) {
    val state by viewModel.uiState.collectAsState()
    val event = state.events.find { it.id == eventId } // ← CORRECTO PARA FIREBASE

    if (event == null) {
        Text(
            text = "Evento no encontrado",
            modifier = Modifier.padding(20.dp),
            color = Color.Red
        )
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        // Imagen principal
        AsyncImage(
            model = event.imageUrl,
            contentDescription = event.title,
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Título
        Text(
            event.title,
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(6.dp))

        // Ubicación
        Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Place,
                contentDescription = "Ubicación",
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                event.location ?: "",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Precio
        Text(
            "Precio: Bs. ${event.price}",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Descripción
        Text(
            event.description ?: "Sin descripción disponible.",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
