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
import com.turismoapp.mayuandino.ui.theme.PurpleMayu

@Composable
fun EventDetailScreen(
    eventId: String,
    viewModel: CalendarViewModel
) {
    val event = viewModel.getEventById(eventId)

    if (event == null) {
        Column(Modifier.fillMaxSize().padding(20.dp)) {
            Text(
                text = "Evento no encontrado",
                color = Color.Red,
                style = MaterialTheme.typography.titleMedium
            )
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        AsyncImage(
            model = event.imageUrl,
            contentDescription = event.title,
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp),
            contentScale = ContentScale.Crop
        )

        Spacer(Modifier.height(20.dp))

        Text(
            event.title,
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(Modifier.height(6.dp))

        Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Place,
                contentDescription = "Ubicación",
                tint = PurpleMayu
            )
            Spacer(Modifier.width(6.dp))
            Text(event.location ?: "", style = MaterialTheme.typography.bodyMedium)
        }

        Spacer(Modifier.height(12.dp))

        Text(
            "Precio: Bs. ${event.price}",
            style = MaterialTheme.typography.titleMedium,
            color = PurpleMayu
        )

        Spacer(Modifier.height(20.dp))

        Text(
            event.description ?: "Sin descripción disponible.",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
