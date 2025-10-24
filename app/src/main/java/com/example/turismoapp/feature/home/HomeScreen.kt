package com.example.turismoapp.feature.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.turismoapp.Framework.dto.PlaceDto

@Composable
fun HomeRoute(vm: HomeViewModel = viewModel()) {
    val state by vm.ui.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) { vm.load() }
    HomeScreen(state = state, onRetry = { vm.load() })
}

@Composable
fun HomeScreen(state: HomeUiState, onRetry: () -> Unit) {
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Explora la hermosa", style = MaterialTheme.typography.titleLarge)
        Text("Cochabamba!", style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.ExtraBold)

        Spacer(Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Los mejores destinos", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            TextButton(onClick = { /* ver más */ }) { Text("Ver más") }
        }

        Spacer(Modifier.height(8.dp))

        when (state) {
            is HomeUiState.Loading -> CircularProgressIndicator()
            is HomeUiState.Error -> {
                Text(state.message, color = MaterialTheme.colorScheme.error)
                Spacer(Modifier.height(8.dp))
                Button(onClick = onRetry) { Text("Reintentar") }
            }
            is HomeUiState.Success -> DestinationsCarousel(state.places)
        }
    }
}

@Composable
fun DestinationsCarousel(places: List<PlaceDto>) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        items(places) { p -> DestinationCard(p) }
    }
}

@Composable
fun DestinationCard(place: PlaceDto) {
    Card(
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .width(260.dp)
            .height(300.dp)
    ) {
        Column {
            AsyncImage(
                model = place.image ?: "https://picsum.photos/600/400?random=${place.id}",
                contentDescription = place.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(170.dp)
                    .clip(RoundedCornerShape(bottomStart = 0.dp, bottomEnd = 0.dp)),
                contentScale = ContentScale.Crop
            )
            Column(Modifier.padding(12.dp)) {
                Text(place.name, style = MaterialTheme.typography.titleMedium, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Spacer(Modifier.height(4.dp))
                Text(place.city ?: place.department, style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(8.dp))
                AssistChip(onClick = {}, label = { Text("★ ${"%.1f".format(place.rating ?: 4.6)}") })
            }
        }
    }
}
