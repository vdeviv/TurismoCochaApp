package com.turismoapp.mayuandino.feature.profile.presentation

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.turismoapp.mayuandino.feature.profile.domain.model.ProfileModel
import com.turismoapp.mayuandino.feature.profile.presentation.ProfileViewModel
import org.koin.androidx.compose.koinViewModel
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

import androidx.compose.material.icons.automirrored.outlined.ExitToApp
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    profileViewModel: ProfileViewModel = koinViewModel(),
    onEditProfileClick: () -> Unit,
    onSignOut: () -> Unit,
    onDeleteAccount: () -> Unit,
) {
    // 1. Recoger el estado del ViewModel
    val state by profileViewModel.state.collectAsState()

    // 2. Cargar el perfil al inicio (Solo se ejecuta una vez)
    LaunchedEffect(Unit) {
        profileViewModel.loadProfile()
    }

    // Manejar el cierre de sesión/eliminación de cuenta
    LaunchedEffect(state) {
        if (state is ProfileViewModel.ProfileUiState.Deleted) {
            onDeleteAccount()
        }
        // Si signOut es exitoso, también debería haber una navegación
        // que observe el estado de FirebaseAuth en el Activity principal.
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Mi Perfil") },
                actions = {
                    IconButton(onClick = onSignOut) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ExitToApp,
                            contentDescription = "Cerrar Sesión",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            )
        }
    ) { padding ->

        // 3. Manejar los diferentes estados de la UI
        when (state) {
            is ProfileViewModel.ProfileUiState.Loading,
            is ProfileViewModel.ProfileUiState.Init -> {
                LoadingView(padding)
            }
            is ProfileViewModel.ProfileUiState.Error -> {
                val message = (state as ProfileViewModel.ProfileUiState.Error).message
                ErrorView(padding, message, profileViewModel::loadProfile)
            }
            is ProfileViewModel.ProfileUiState.Deleted -> {
                // Ya manejado en el LaunchedEffect
            }
            is ProfileViewModel.ProfileUiState.Success -> {
                val profile = (state as ProfileViewModel.ProfileUiState.Success).profile

                // ⬇️ FUNCIÓN QUE CIERRA SESIÓN Y ACTIVA REDIRECCIÓN ⬇️
                val combinedSignOut: () -> Unit = {
                    profileViewModel.signOut() // 1. Cierra la sesión en Firebase (asíncrono)
                    onSignOut() // 2. Ejecuta la navegación a Login (síncrono)
                }

                SuccessView(
                    padding = padding,
                    profile = profile,
                    onEditProfileClick = onEditProfileClick,
                    onSignOut = combinedSignOut, // ⬅️ Pasa la función combinada
                    onDeleteAccount = { profileViewModel.deleteAccount() }
                )
            }
        }
    }
}

// ----------------------------------------------------
// Componentes Reutilizables de la UI
// ----------------------------------------------------

@Composable
private fun SuccessView(
    padding: PaddingValues,
    profile: ProfileModel,
    onEditProfileClick: () -> Unit,
    onSignOut: () -> Unit,
    onDeleteAccount: () -> Unit
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(20.dp))

        // 🖼️ FOTO DE PERFIL
        AsyncImage(
            model = profile.pathUrl,
            contentDescription = "Foto de perfil",
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .border(2.dp, Color.LightGray, CircleShape),
            contentScale = ContentScale.Crop
        )

        Spacer(Modifier.height(16.dp))

        // 👤 NOMBRE Y EMAIL
        Text(
            profile.name.ifEmpty { "Viajero Anónimo" },
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            profile.email,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )

        // 📝 Resumen / Descripción
        if (profile.summary.isNotEmpty()) {
            Spacer(Modifier.height(12.dp))
            Text(
                profile.summary,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }


        Spacer(Modifier.height(28.dp))

        // 📊 ESTADÍSTICAS (Placeholder)
        StatsCard()

        Spacer(Modifier.height(28.dp))

        // 📋 MENÚ DE OPCIONES
        Column(modifier = Modifier.fillMaxWidth()) {
            Text("General", style = MaterialTheme.typography.titleMedium, color = Color.Gray)
            Spacer(Modifier.height(8.dp))

            MenuOption(
                title = "Editar Perfil",
                icon = Icons.Outlined.Person,
                onClick = onEditProfileClick
            )
            MenuOption(
                title = "Mis Viajes",
                icon = Icons.Outlined.Place,
                onClick = { /* TODO: Navegar a Mis Viajes */ }
            )
            MenuOption(
                title = "Lista de Deseos",
                icon = Icons.Outlined.FavoriteBorder,
                onClick = { /* TODO: Navegar a Lista de Deseos */ }
            )

            // Opciones de cuenta
            Spacer(Modifier.height(16.dp))
            Text("Opciones de cuenta", style = MaterialTheme.typography.titleMedium, color = Color.Gray)
            Spacer(Modifier.height(8.dp))


            MenuOption(
                title = "Eliminar Cuenta",
                icon = Icons.Outlined.Delete,
                isDestructive = true,
                onClick = onDeleteAccount
            )

        }

    }
}

@Composable
private fun StatsCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            StatItem(value = "10", label = "Viajes")
            StatItem(value = "45", label = "Favoritos")
            StatItem(value = "9", label = "Reseñas")
        }
    }
}

@Composable
private fun StatItem(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Text(label, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
    }
}

@Composable
private fun MenuOption(title: String, icon: ImageVector, onClick: () -> Unit, isDestructive: Boolean = false) {
    val color = if (isDestructive) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = color)
        Spacer(Modifier.width(16.dp))
        Text(
            title,
            style = MaterialTheme.typography.bodyLarge,
            color = color,
            modifier = Modifier.weight(1f)
        )
        if (!isDestructive) {
            Icon(Icons.Outlined.KeyboardArrowRight, contentDescription = null, tint = Color.Gray)
        }
    }
}

@Composable
private fun LoadingView(padding: PaddingValues) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorView(padding: PaddingValues, message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Error al cargar el perfil", style = MaterialTheme.typography.titleLarge, color = Color.Red)
        Spacer(Modifier.height(8.dp))
        Text(message, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
        Spacer(Modifier.height(16.dp))
        Button(onClick = onRetry) {
            Text("Reintentar")
        }
    }
}