package com.example.turismoapp.feature.profile.presentation

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import com.example.turismoapp.feature.profile.domain.model.ProfileModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    profileViewModel: ProfileViewModel = koinViewModel(),
    onBack: () -> Unit
) {
    val state by profileViewModel.state.collectAsState()
    val isSaving = remember { mutableStateOf(false) }
    val showSuccessDialog = remember { mutableStateOf(false) }

    // Carga inicial: Asegura que el perfil se cargue al entrar a la pantalla
    LaunchedEffect(Unit) {
        profileViewModel.loadProfile()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Perfil") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { padding ->

        when (state) {
            is ProfileViewModel.ProfileUiState.Loading,
            ProfileViewModel.ProfileUiState.Init -> {
                Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is ProfileViewModel.ProfileUiState.Error -> {
                Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                    Text(
                        "Error al cargar/guardar: ${(state as ProfileViewModel.ProfileUiState.Error).message}",
                        color = Color.Red,
                        modifier = Modifier.padding(24.dp)
                    )
                }
            }
            is ProfileViewModel.ProfileUiState.Success -> {
                // Solo renderizamos si el perfil se cargó correctamente
                EditProfileForm(
                    padding = padding,
                    profile = (state as ProfileViewModel.ProfileUiState.Success).profile,
                    viewModel = profileViewModel,
                    isSaving = isSaving,
                    showSuccessDialog = showSuccessDialog,
                    onBack = onBack
                )
            }
            else -> {}
        }
    }

    // Diálogo de éxito (se muestra sobre el Scaffold)
    if (showSuccessDialog.value) {
        AlertDialog(
            onDismissRequest = {
                showSuccessDialog.value = false
                onBack()
            },
            title = { Text("¡Perfil actualizado!") },
            text = { Text("Tus cambios se han guardado correctamente.") },
            confirmButton = {
                TextButton(onClick = {
                    showSuccessDialog.value = false
                    onBack()
                }) {
                    Text("Aceptar")
                }
            }
        )
    }
}

// Sub-Composable para la lógica de la forma de edición
@Composable
private fun EditProfileForm(
    padding: PaddingValues,
    profile: ProfileModel,
    viewModel: ProfileViewModel,
    isSaving: MutableState<Boolean>,
    showSuccessDialog: MutableState<Boolean>,
    onBack: () -> Unit
) {
    // 1. Estados locales para los campos, NO inicializados aquí
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var summary by remember { mutableStateOf("") }
    var avatarUrl by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val isInitialized = remember { mutableStateOf(false) } // Bandera para inicialización

    val state by viewModel.state.collectAsState()

    // 2. SINCRONIZACIÓN DE ESTADOS (Solo se ejecuta si el perfil cambia)
    LaunchedEffect(profile) {
        // Solo inicializamos los estados locales si no se han cargado antes
        if (!isInitialized.value) {
            name = profile.name
            email = profile.email // Email cargado desde el ProfileModel
            phone = profile.cellphone
            summary = profile.summary
            avatarUrl = profile.pathUrl
            isInitialized.value = true
        }
    }

    // Observar el estado de guardado
    LaunchedEffect(state) {
        if (state is ProfileViewModel.ProfileUiState.Success) {
            if (isSaving.value) {
                isSaving.value = false
                showSuccessDialog.value = true
            }
        }
        if (state is ProfileViewModel.ProfileUiState.Error) {
            isSaving.value = false
        }
    }


    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
        if (uri != null) {
            viewModel.uploadNewAvatar(uri)
            isSaving.value = true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // ... (Resto del código de la UI es idéntico a la versión anterior)

        // 📸 Sección de Avatar
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                .clickable { imagePickerLauncher.launch("image/*") }
        ) {
            val imageSource = selectedImageUri ?: if (avatarUrl.isNotEmpty()) avatarUrl else "https://placehold.co/100x100/9C27B0/ffffff?text=AV"
            AsyncImage(
                model = imageSource,
                contentDescription = "Avatar de Usuario",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Text(
                "Cambiar",
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .padding(vertical = 4.dp),
                color = Color.White,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.height(32.dp))

        // 📝 Campos de Edición
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nombre Completo") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isSaving.value
        )
        Spacer(Modifier.height(16.dp))

        // Campo de Email (Solo Lectura)
        OutlinedTextField(
            value = email, // Ahora toma el valor del estado local sincronizado
            onValueChange = { /* No permitir cambiar el email */ },
            label = { Text("Correo Electrónico (No editable)") },
            readOnly = true,
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                disabledLabelColor = Color.Gray,
                disabledTextColor = Color.DarkGray,
                disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                disabledBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.7f)
            )
        )
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Número de Teléfono") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isSaving.value
        )
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = summary,
            onValueChange = { summary = it },
            label = { Text("Breve Biografía/Resumen") },
            singleLine = false,
            maxLines = 4,
            modifier = Modifier.fillMaxWidth(),
            enabled = !isSaving.value
        )
        Spacer(Modifier.height(32.dp))

        // 💾 Botón de Guardar
        Button(
            onClick = {
                if (!isSaving.value && state is ProfileViewModel.ProfileUiState.Success) {
                    // 1. Marcar como guardando
                    isSaving.value = true
                    // 2. Llamar al ViewModel con los datos locales
                    // Pasamos el email local, que contiene el email de Auth
                    viewModel.updateProfile(name, email, phone, summary)
                }
            },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            enabled = !isSaving.value && state is ProfileViewModel.ProfileUiState.Success, // Deshabilitar si no está en estado Success
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            if (isSaving.value) {
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

        // Mostrar errores
        if (state is ProfileViewModel.ProfileUiState.Error) {
            Spacer(Modifier.height(16.dp))
            Text(
                text = "Error: ${(state as ProfileViewModel.ProfileUiState.Error).message}",
                color = Color.Red,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}