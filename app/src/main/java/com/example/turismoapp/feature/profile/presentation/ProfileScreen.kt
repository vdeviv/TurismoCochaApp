package com.example.turismoapp.feature.profile.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileScreen(
    profileViewModel: ProfileViewModel = koinViewModel(),
    onEditProfile: () -> Unit = {},
    onFavorites: () -> Unit = {},
    onTrips: () -> Unit = {},
    onSettings: () -> Unit = {},
    onLanguage: () -> Unit = {}
) {
    val state = profileViewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        profileViewModel.showProfile()
    }

    when (val st = state.value) {
        is ProfileViewModel.ProfileUiState.Error -> ErrorView(st.message)
        ProfileViewModel.ProfileUiState.Init -> {}
        ProfileViewModel.ProfileUiState.Loading -> LoadingView()
        is ProfileViewModel.ProfileUiState.Success -> {
            ProfileContent(
                name = st.profile.name,
                email = st.profile.email,
                cellphone = st.profile.cellphone,
                summary = st.profile.summary,
                avatarUrl = st.profile.pathUrl,
                onEditProfile = onEditProfile,
                onFavorites = onFavorites,
                onTrips = onTrips,
                onSettings = onSettings,
                onLanguage = onLanguage
            )
        }
    }
}

@Composable
private fun ProfileContent(
    name: String,
    email: String,
    cellphone: String,
    summary: String,
    avatarUrl: String,
    onEditProfile: () -> Unit,
    onFavorites: () -> Unit,
    onTrips: () -> Unit,
    onSettings: () -> Unit,
    onLanguage: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // 🔹 Header con imagen y nombre
        AsyncImage(
            model = avatarUrl,
            contentDescription = "Foto de perfil de $name",
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(Color(0xFFF0F0F0)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = name,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )
        Text(text = email, color = Color.Gray, style = MaterialTheme.typography.bodySmall)

        Spacer(modifier = Modifier.height(16.dp))

        // 🔹 Tarjeta de estadísticas
        ProfileStats(points = 360, trips = 238, wishlist = 473)

        Spacer(modifier = Modifier.height(24.dp))

        // 🔹 Opciones
        ProfileOption(icon = Icons.Default.Edit, text = "Editar perfil", onClick = onEditProfile)
        ProfileOption(icon = Icons.Outlined.FavoriteBorder, text = "Favoritos", onClick = onFavorites)
        ProfileOption(icon = Icons.Outlined.History, text = "Viajes previos", onClick = onTrips)
        ProfileOption(icon = Icons.Outlined.Settings, text = "Ajustes", onClick = onSettings)
        ProfileOption(icon = Icons.Outlined.Language, text = "Idioma", onClick = onLanguage)

        Spacer(modifier = Modifier.height(20.dp))

        // 🔹 Resumen o descripción del usuario
        if (summary.isNotBlank() || cellphone.isNotBlank()) {
            Text(
                text = listOf(summary, cellphone).filter { it.isNotBlank() }.joinToString(" • "),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
fun ProfileStats(points: Int, trips: Int, wishlist: Int) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F9F9)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StatItem("Puntos de viaje", points)
            Divider(
                color = Color(0xFFEAEAEA),
                modifier = Modifier
                    .width(1.dp)
                    .height(40.dp)
            )
            StatItem("Viajes", trips)
            Divider(
                color = Color(0xFFEAEAEA),
                modifier = Modifier
                    .width(1.dp)
                    .height(40.dp)
            )
            StatItem("Lista de deseos", wishlist)
        }
    }
}

@Composable
private fun StatItem(label: String, value: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value.toString(),
            color = Color(0xFF7B3FA1),
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.titleMedium
        )
        Text(text = label, color = Color.Gray, style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
private fun ProfileOption(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = text, tint = Color(0xFF333333))
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = text, style = MaterialTheme.typography.bodyLarge)
    }
    Divider(color = Color(0xFFEAEAEA))
}

@Composable
private fun ErrorView(message: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Error: $message", color = Color.Red)
    }
}

@Composable
private fun LoadingView() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}
