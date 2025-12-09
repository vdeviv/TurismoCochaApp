package com.turismoapp.mayuandino.feature.packages.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.turismoapp.mayuandino.feature.packages.domain.model.PackageModel

@Composable
fun PackageDetailScreen(
    pkg: PackageModel,
    onBack: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {

        // Imagen + botón atrás
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
        ) {
            AsyncImage(
                model = pkg.imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Volver",
                    tint = Color.White
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {

            Text(
                text = pkg.title,
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "⭐ ${pkg.rating}",
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = pkg.description,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(Modifier.height(24.dp))

            Text(
                text = "Ubicación: ${pkg.city}",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(Modifier.height(32.dp))

            Button(
                onClick = { /* TODO: Acción de reservar */ },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF7B2CE5)
                )
            ) {
                Text("Reservar paquete")
            }
        }
    }
}
