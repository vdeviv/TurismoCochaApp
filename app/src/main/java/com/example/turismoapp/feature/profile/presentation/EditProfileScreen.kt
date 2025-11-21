package com.example.turismoapp.feature.profile.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    profileViewModel: ProfileViewModel = koinViewModel(),
    // ✅ CORRECCIÓN 1: onSave ahora es una función sin argumentos (para navegación)
    onSave: () -> Unit
) {
    var isSaving by remember { mutableStateOf(false) }
    val state = profileViewModel.state.collectAsState()

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var summary by remember { mutableStateOf("") }
    var avatarUrl by remember { mutableStateOf("https://cdn-icons-png.flaticon.com/512/149/149071.png") }

    var showSuccessDialog by remember { mutableStateOf(false) }

    // Cargar datos del perfil actual
    LaunchedEffect(state.value) {
        if (state.value is ProfileViewModel.ProfileUiState.Success) {
            val profile = (state.value as ProfileViewModel.ProfileUiState.Success).profile
            name = profile.name
            email = profile.email
            phone = profile.cellphone
            summary = profile.summary
            avatarUrl = profile.pathUrl.ifBlank { "https://cdn-icons-png.flaticon.com/512/149/149071.png" }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Editar Perfil") },
                navigationIcon = {
                    IconButton(onClick = onSave) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(Modifier.height(20.dp))

            // FOTO DE PERFIL
            AsyncImage(
                model = avatarUrl,
                contentDescription = "Foto de perfil",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color.LightGray, CircleShape)
                    .clickable {
                        // Aquí podrías abrir un selector de imágenes
                    },
                contentScale = ContentScale.Crop
            )

            Text(
                "Cambiar foto",
                color = Color(0xFF9C27B0),
                modifier = Modifier.clickable { },
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(Modifier.height(28.dp))

            // CAMPOS DE TEXTO
            OutlinedTextField(
                value = name,
                onValueChange = { name = it }, // ✅ Esto permite editar el nombre
                label = { Text("Nombre completo") },
                modifier = Modifier.fillMaxWidth(),
                // 💡 Ajuste de estado:
                enabled = state.value !is ProfileViewModel.ProfileUiState.Loading // Solo deshabilitar durante la carga
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo electrónico") },
                modifier = Modifier.fillMaxWidth(),
                isError = email.isNotBlank() && !email.contains("@"),
                supportingText = {
                    if (email.isNotBlank() && !email.contains("@"))
                        Text("Email inválido", color = Color.Red)
                },
                enabled = state.value !is ProfileViewModel.ProfileUiState.Loading
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Teléfono") },
                modifier = Modifier.fillMaxWidth(),
                enabled = state.value !is ProfileViewModel.ProfileUiState.Loading
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = summary,
                onValueChange = { summary = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 5,
                enabled = state.value !is ProfileViewModel.ProfileUiState.Loading
            )

            Spacer(Modifier.height(28.dp))

            // BOTÓN GUARDAR
            Button(
                onClick = {
                    isSaving = true // 💡 Inicia el estado de guardado
                    profileViewModel.updateProfile(name, email, phone, summary)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9C27B0)),
                enabled = name.isNotBlank() &&
                        email.contains("@") &&
                        state.value !is ProfileViewModel.ProfileUiState.Loading
            ) {
                if (state.value is ProfileViewModel.ProfileUiState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White
                    )
                } else {
                    Text(
                        "Guardar cambios",
                        color = Color.White,
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }

            // Mostrar errores si hay
            if (state.value is ProfileViewModel.ProfileUiState.Error) {
                Spacer(Modifier.height(16.dp))
                Text(
                    text = (state.value as ProfileViewModel.ProfileUiState.Error).message,
                    color = Color.Red,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }

    // Diálogo de éxito
    if (showSuccessDialog && state.value is ProfileViewModel.ProfileUiState.Success) {
        AlertDialog(
            onDismissRequest = {
                showSuccessDialog = false
                onSave()
            },
            title = { Text("¡Perfil actualizado!") },
            text = { Text("Tus cambios se han guardado correctamente.") },
            confirmButton = {
                TextButton(onClick = {
                    showSuccessDialog = false
                    onSave()
                }) {
                    Text("Aceptar")
                }
            }
        )
    }
}