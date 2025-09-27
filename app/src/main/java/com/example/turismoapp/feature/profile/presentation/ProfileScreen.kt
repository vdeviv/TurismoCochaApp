package com.example.turismoapp.feature.profile.presentation


import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.turismoapp.feature.profile.presentation.ProfileViewModel
import com.example.turismoapp.feature.navigation.Screen
import org.koin.androidx.compose.koinViewModel

import org.koin.androidx.compose.koinViewModel


@Composable
fun ProfileScreen(
    profileViewModel: ProfileViewModel = koinViewModel()
) {
    val state = profileViewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        profileViewModel.showProfile()
    }

    when(val st = state.value) {
        is ProfileViewModel.ProfileUiState.Error -> Text(st.message)
        ProfileViewModel.ProfileUiState.Init -> Text("")
        ProfileViewModel.ProfileUiState.Loading -> CircularProgressIndicator()
        is ProfileViewModel.ProfileUiState.Success -> {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AsyncImage(
                    model = st.profile.pathUrl,
                    contentDescription = "Foto de perfil de ${st.profile.name}",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape) // Opcional: imagen circular
                        .border(2.dp, Color.Gray, CircleShape),
                    contentScale = ContentScale.Crop
                )

                Text(
                    text = st.profile.name,
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = st.profile.email,
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = st.profile.cellphone,
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = st.profile.summary,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 8.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }


}