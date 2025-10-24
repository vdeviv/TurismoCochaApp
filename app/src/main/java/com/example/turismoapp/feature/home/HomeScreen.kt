package com.example.turismoapp.feature.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.turismoapp.Framework.dto.PlaceDto
import com.example.turismoapp.R

@Composable
fun HomeScreen(
    state: HomeUiState,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // ---------- Header con perfil y botón ----------
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = "Foto de perfil",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                )
                Spacer(Modifier.width(10.dp))
                Text(
                    text = "Jairo Montaño",
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold)
                )
            }

            IconButton(onClick = { /* acción compartir */ }) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "Compartir",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        // ---------- Título principal ----------
        Text(
            text = "Explora la hermosa",
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = "Cochabamba!",
            style = MaterialTheme.typography.displaySmall.copy(
                color = Color(0xFFD32F2F),
                fontWeight = FontWeight.ExtraBold
            )
        )
        Divider(
            color = Color(0xFFD32F2F),
            thickness = 3.dp,
            modifier = Modifier
                .width(150.dp)
                .padding(vertical = 4.dp)
        )

        Spacer(Modifier.height(16.dp))

        // ---------- Sección de mejores destinos ----------
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Los mejores destinos",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
            )
            TextButton(onClick = { /* Ver más */ }) {
                Text("Ver más", color = MaterialTheme.colorScheme.primary)
            }
        }

        Spacer(Modifier.height(8.dp))

        // ---------- Cuerpo según estado ----------
        when (state) {
            is HomeUiState.Loading -> {
                Box(
                    Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is HomeUiState.Error -> {
                Box(
                    Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = state.message,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(Modifier.height(8.dp))
                        Button(onClick = onRetry) {
                            Text("Reintentar")
                        }
                    }
                }
            }

            is HomeUiState.Success -> {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(state.places) { place ->
                        DestinationCard(place)
                    }
                }
            }
        }
    }
}

// ---------- Tarjeta de destino individual ----------
@Composable
fun DestinationCard(place: PlaceDto) {
    Card(
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .width(260.dp)
            .height(300.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column {
            AsyncImage(
                model = place.image ?: "https://picsum.photos/600/400?random=${place.id}",
                contentDescription = place.name ?: "",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(170.dp)
                    .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)),
                contentScale = ContentScale.Crop
            )

            Column(Modifier.padding(12.dp)) {
                Text(
                    text = place.name ?: "Sin nombre",
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = place.city ?: place.department ?: "Sin ubicación",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(Modifier.height(8.dp))
                AssistChip(onClick = {}, label = {
                    Text("★ ${"%.1f".format(place.rating ?: 4.6)}")
                })
            }
        }
    }
}
