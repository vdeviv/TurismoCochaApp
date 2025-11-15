package com.example.turismoapp.feature.search.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.turismoapp.Framework.dto.PlaceDto
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.CameraPositionState
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.CameraPosition

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaceDetailScreen(
    place: PlaceDto,
    onBack: () -> Unit
) {
    var isFavorite by remember { mutableStateOf(false) }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(place.latitude, place.longitude),
            15f
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(270.dp)
        ) {
            AsyncImage(
                model = place.image,
                contentDescription = place.name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            IconButton(
                onClick = { onBack() },
                modifier = Modifier
                    .padding(14.dp)
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.6f))
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
        }

        Column(modifier = Modifier.padding(20.dp)) {

            Text(place.name, style = MaterialTheme.typography.headlineSmall)
            Text(place.city, style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.height(8.dp))

            Text(" ${place.rating}", style = MaterialTheme.typography.bodyLarge)

            Spacer(modifier = Modifier.height(16.dp))

            Text(place.description, style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Ubicación",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(12.dp))

            // GOOGLE MAPS
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            ) {
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState
                ) {
                    Marker(
                        state = MarkerState(
                            position = LatLng(place.latitude, place.longitude)
                        ),
                        title = place.name
                    )
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = {  }) {
                    Text("Ver en Google Maps")
                }

                IconButton(onClick = { isFavorite = !isFavorite }) {
                    Icon(
                        imageVector = if (isFavorite)
                            Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "fav"
                    )
                }
            }
        }
    }
}
