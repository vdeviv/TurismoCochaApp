package com.example.turismoapp.feature.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.turismoapp.Framework.dto.PlaceDto
import com.example.turismoapp.R
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState

@Composable
fun HomeScreen(
    state: HomeUiState, // El estado que viene de la observación
    onRetry: () -> Unit,
    onProfileClick: () -> Unit,
    onNotificationClick: () -> Unit,
    onPlaceClick: (String) -> Unit,
    viewModel: HomeViewModel = viewModel()
)
{
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 20.dp)
    ) {

        // ------------ HEADER ------------
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp, bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { onProfileClick() }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = "Perfil",
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                )
                Spacer(Modifier.width(12.dp))
                Text(
                    text = "Jairo Montaño",
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold)
                )
            }

            IconButton(onClick = { onNotificationClick() }) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notificaciones",
                    tint = Color.Black,
                    modifier = Modifier.size(26.dp)
                )
            }
        }

        // ---------- Título ----------
        Text(
            text = "Explora la hermosa",
            style = MaterialTheme.typography.titleLarge.copy(
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold
            )
        )
        Text(
            text = "Cochabamba!",
            style = MaterialTheme.typography.displaySmall.copy(
                color = Color(0xFFD32F2F),
                fontWeight = FontWeight.ExtraBold
            )
        )
        HorizontalDivider(
            color = Color(0xFFD32F2F),
            thickness = 3.dp,
            modifier = Modifier
                .width(160.dp)
                .padding(top = 6.dp, bottom = 24.dp)
        )

        // ---------- Mejores destinos ----------
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Los mejores destinos",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
            )
            Text(
                "Ver más",
                color = Color(0xFF7A1CAC),
                modifier = Modifier.clickable { },
            )
        }

        Spacer(Modifier.height(12.dp))

        when (state) {
            is HomeUiState.Loading -> Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) { CircularProgressIndicator() }

            is HomeUiState.Error -> Column(
                Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(state.message, color = Color.Red)
                Button(onClick = onRetry) { Text("Reintentar") }
            }

            is HomeUiState.Success -> {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                    items(state.places) { place ->
                        DestinationCard(
                            place = place,
                            onClick = { onPlaceClick(place.id) }
                        )
                    }
                }
            }
        }
    }
}

// ===========================================================
//  CARD estilo Figma — versión final y corregida
// ===========================================================
@Composable
fun DestinationCard(place: PlaceDto, onClick: () -> Unit) {
    var isBookmarked by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier
            .width(280.dp)     // ancho exacto según Figma
            .height(380.dp)    // alto ideal para pantallas reales
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Box {
            Column {

                // Imagen grande, como Figma
                AsyncImage(
                    model = place.image,
                    contentDescription = place.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(230.dp),
                    contentScale = ContentScale.Crop
                )

                Column(Modifier.padding(16.dp)) {
                    Text(
                        place.name,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(Modifier.height(6.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = android.R.drawable.ic_menu_mylocation),
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(place.city, color = Color.Gray)
                    }

                    Spacer(Modifier.height(12.dp))

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFFFF3E0)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("⭐", fontSize = 14.sp)
                            Spacer(Modifier.width(6.dp))
                            Text(
                                "%.1f".format(place.rating),
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFFF6F00)
                            )
                        }
                    }
                }
            }

            // Bookmark button
            IconButton(
                onClick = { isBookmarked = !isBookmarked },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp)
                    .size(36.dp)
                    .background(Color.White.copy(alpha = 0.9f), CircleShape)
            ) {
                Icon(
                    imageVector = if (isBookmarked)
                        Icons.Default.Bookmark
                    else
                        Icons.Default.BookmarkBorder,
                    contentDescription = "Bookmark",
                    tint = if (isBookmarked) Color(0xFFD32F2F) else Color.Gray
                )
            }
        }
    }
}
