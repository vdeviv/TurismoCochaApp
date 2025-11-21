package com.example.turismoapp.feature.profile.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileScreen(
    profileViewModel: ProfileViewModel = koinViewModel(),
    onBack: () -> Unit,
    onEditProfile: () -> Unit,
    onFavorites: () -> Unit,
    onTrips: () -> Unit,
    onSettings: () -> Unit,
    onLanguage: () -> Unit
) {
    val state by profileViewModel.state.collectAsState()

    var name by remember { mutableStateOf(TextFieldValue("")) }
    var phone by remember { mutableStateOf(TextFieldValue("")) }
    var summary by remember { mutableStateOf(TextFieldValue("")) }

    LaunchedEffect(Unit) { profileViewModel.loadProfile() }

    Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        when (state) {
            is ProfileViewModel.ProfileUiState.Loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            is ProfileViewModel.ProfileUiState.Error -> Text(
                text = (state as ProfileViewModel.ProfileUiState.Error).message,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.align(Alignment.Center)
            )
            is ProfileViewModel.ProfileUiState.Success -> {
                val profile = (state as ProfileViewModel.ProfileUiState.Success).profile
                LaunchedEffect(profile) {
                    name = TextFieldValue(profile.name)
                    phone = TextFieldValue(profile.cellphone)
                    summary = TextFieldValue(profile.summary)
                }

                Column(modifier = Modifier.fillMaxSize()) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = rememberAsyncImagePainter(
                                ImageRequest.Builder(LocalContext.current)
                                    .data(profile.pathUrl)
                                    .crossfade(true)
                                    .build()
                            ),
                            contentDescription = "Avatar",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.size(80.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(text = profile.email)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text("Nombre")
                    BasicTextField(value = name, onValueChange = { name = it }, modifier = Modifier.fillMaxWidth().padding(4.dp))

                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Teléfono")
                    BasicTextField(value = phone, onValueChange = { phone = it }, modifier = Modifier.fillMaxWidth().padding(4.dp))

                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Resumen")
                    BasicTextField(value = summary, onValueChange = { summary = it }, modifier = Modifier.fillMaxWidth().height(100.dp).padding(4.dp))

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(onClick = { profileViewModel.updateProfile(name.text, profile.email, phone.text, summary.text) }) {
                        Text("Guardar Cambios")
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
            else -> {}
        }
    }
}
