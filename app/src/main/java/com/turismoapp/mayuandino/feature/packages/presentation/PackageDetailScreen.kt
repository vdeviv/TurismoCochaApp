package com.turismoapp.mayuandino.feature.packages.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.turismoapp.mayuandino.R
import com.turismoapp.mayuandino.feature.packages.domain.model.PackageModel
import com.turismoapp.mayuandino.ui.theme.*

@Composable
fun PackageDetailScreen(
    pkg: PackageModel,
    onBack: () -> Unit
) {

    Column(modifier = Modifier.fillMaxSize()) {

        // ------------------ IMAGEN + BACK ------------------
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(310.dp)
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
                    .padding(16.dp)
                    .background(Color(0x66000000), CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Volver",
                    tint = Color.White
                )
            }
        }

        // ------------------ CONTENIDO ------------------
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 22.dp, vertical = 18.dp)
        ) {

            // TITULO
            Text(
                text = pkg.title,
                style = MaterialTheme.typography.headlineSmall.copy(color = TextBlack)
            )

            Spacer(Modifier.height(8.dp))

            // RATING
            Text(
                text = "⭐ ${pkg.rating}",
                style = MaterialTheme.typography.bodyLarge.copy(color = YellowMayu)
            )

            Spacer(Modifier.height(14.dp))

            // DESCRIPCIÓN
            Text(
                text = pkg.description,
                style = MaterialTheme.typography.bodyMedium.copy(color = GrayText)
            )

            Spacer(Modifier.height(18.dp))

            // UBICACIÓN
            Text(
                text = "📍 Ubicación: ${pkg.city}",
                style = MaterialTheme.typography.bodyMedium.copy(color = TextBlack)
            )

            Spacer(Modifier.height(22.dp))

            // ------------------ PARTICIPANTES ------------------
            Text(
                text = "Personas que se unieron",
                style = MaterialTheme.typography.bodyMedium.copy(color = TextBlack)
            )

            Spacer(Modifier.height(8.dp))

            val participants = pkg.joinedUsers.take(5)

            Row(verticalAlignment = Alignment.CenterVertically) {
                if (participants.isEmpty()) {
                    repeat(3) {
                        Image(
                            painter = painterResource(id = R.drawable.avatar_placeholder),
                            contentDescription = null,
                            modifier = Modifier
                                .size(30.dp)
                                .background(Color.LightGray, CircleShape)
                                .padding(2.dp)
                        )
                        Spacer(Modifier.width(6.dp))
                    }
                } else {
                    participants.forEach { url ->
                        AsyncImage(
                            model = url,
                            contentDescription = null,
                            modifier = Modifier
                                .size(32.dp)
                                .background(Color.White, CircleShape),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(Modifier.width(6.dp))
                    }
                }
            }

            Spacer(Modifier.height(40.dp))

            // BOTÓN DE RESERVA
            Button(
                onClick = { /* TODO */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PurpleMayu)
            ) {
                Text(
                    text = "Reservar paquete",
                    style = MaterialTheme.typography.titleMedium.copy(color = Color.White)
                )
            }
        }
    }
}
