package com.example.turismoapp.feature.profile.presentation

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import org.koin.androidx.compose.koinViewModel

@Composable
fun EditProfileScreen(
    profileViewModel: ProfileViewModel = koinViewModel(),
    onBack: () -> Unit
) {
    val state by profileViewModel.state.collectAsState()

    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var summary by remember { mutableStateOf("") }
    var avatarUrl by remember { mutableStateOf("https://cdn-icons-png.flaticon.com/512/149/149071.png") }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            avatarUrl = it.toString()
            profileViewModel.uploadNewAvatar(it)
        }
    }

    LaunchedEffect(state) {
        val profile = (state as? ProfileViewModel.ProfileUiState.Success)?.profile
        profile?.let {
            name = it.name
            phone = it.cellphone
            summary = it.summary
            avatarUrl = it.pathUrl
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        AsyncImage(model = avatarUrl, contentDescription = "Avatar", modifier = Modifier.size(120.dp).clip(CircleShape).clickable { imagePickerLauncher.launch("image/*") })
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Teléfono") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = summary, onValueChange = { summary = it }, label = { Text("Resumen") }, modifier = Modifier.fillMaxWidth().height(100.dp))

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            val email = (state as? ProfileViewModel.ProfileUiState.Success)?.profile?.email ?: ""
            profileViewModel.updateProfile(name, email, phone, summary)
        }, modifier = Modifier.fillMaxWidth()) {
            Text("Guardar cambios")
        }

        if (state is ProfileViewModel.ProfileUiState.Error) {
            Spacer(modifier = Modifier.height(8.dp))
            Text((state as ProfileViewModel.ProfileUiState.Error).message, color = MaterialTheme.colorScheme.error)
        }
    }
}
