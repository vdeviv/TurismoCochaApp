package com.turismoapp.mayuandino.feature.home.presentation

import android.app.Application
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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.turismoapp.mayuandino.feature.home.viewmodel.HomeUiState
import com.turismoapp.mayuandino.feature.home.viewmodel.HomeViewModel
import com.turismoapp.mayuandino.feature.home.viewmodel.HomeViewModelFactory
import com.turismoapp.mayuandino.framework.dto.PlaceDto
import com.turismoapp.mayuandino.R
import com.turismoapp.mayuandino.feature.profile.presentation.ProfileViewModel
import org.koin.androidx.compose.koinViewModel

// COLORES MAYU
import com.turismoapp.mayuandino.ui.theme.RedMayu
import com.turismoapp.mayuandino.ui.theme.YellowMayu
import com.turismoapp.mayuandino.ui.theme.GreenMayu
import com.turismoapp.mayuandino.ui.theme.PurpleMayu
import com.turismoapp.mayuandino.ui.theme.BeigeMayu
import com.turismoapp.mayuandino.ui.theme.TextBlack
import com.turismoapp.mayuandino.ui.theme.GrayText
import com.turismoapp.mayuandino.ui.theme.WhiteBackground

@Composable
fun HomeScreen(
    onProfileClick: () -> Unit,
    onNotificationClick: () -> Unit,
    onPlaceClick: (String) -> Unit
) {

    // ---------------- HOME VIEWMODEL ----------------
    val context = LocalContext.current
    val application = context.applicationContext as Application
    val homeVM: HomeViewModel = viewModel(factory = HomeViewModelFactory(application))
    val state by homeVM.ui.collectAsState()

    LaunchedEffect(Unit) {
        homeVM.refresh()
    }

    // ---------------- PROFILE VIEWMODEL ----------------
    val profileVM: ProfileViewModel = koinViewModel()
    val profileState by profileVM.state.collectAsState()

    LaunchedEffect(Unit) {
        profileVM.loadProfile()
    }

    // ---------------- DATOS DINÁMICOS DEL PERFIL ----------------

    val (displayName, photoUrl) = when (profileState) {
        is ProfileViewModel.ProfileUiState.Success -> {
            val p = (profileState as ProfileViewModel.ProfileUiState.Success).profile
            val name = p.name.ifBlank { "Viajero Anónimo" }
            val url = p.pathUrl.ifBlank { "https://cdn-icons-png.flaticon.com/512/149/149071.png" }
            name to url
        }
        else -> {
            "Viajero Anónimo" to "https://cdn-icons-png.flaticon.com/512/149/149071.png"
        }
    }

    // ---------------------------- UI ------------------------------------------

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WhiteBackground)
            .padding(horizontal = 20.dp)
    ) {

        // ---------------- HEADER DINÁMICO ----------------
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp, bottom = 16.dp)
                .clickable { onProfileClick() },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Row(verticalAlignment = Alignment.CenterVertically) {

                AsyncImage(
                    model = photoUrl,
                    contentDescription = "Foto de perfil",
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

                Spacer(Modifier.width(12.dp))

                Text(
                    text = displayName,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = TextBlack
                    )
                )
            }

            IconButton(onClick = onNotificationClick) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notificaciones",
                    tint = TextBlack,
                    modifier = Modifier.size(26.dp)
                )
            }
        }

        // ---------- TITULOS ----------
        Text(
            text = "Explora la hermosa",
            style = MaterialTheme.typography.titleLarge.copy(
                fontSize = 22.sp,
                color = TextBlack,
                fontWeight = FontWeight.SemiBold
            )
        )
        Text(
            text = "Cochabamba!",
            style = MaterialTheme.typography.displaySmall.copy(
                color = RedMayu,
                fontWeight = FontWeight.ExtraBold
            )
        )
        HorizontalDivider(
            color = RedMayu,
            thickness = 3.dp,
            modifier = Modifier
                .width(160.dp)
                .padding(top = 6.dp, bottom = 24.dp)
        )

        // ---------- SUBTITULO ----------
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Los mejores destinos",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = TextBlack
                )
            )
            Text(
                "Ver más",
                color = PurpleMayu,
                modifier = Modifier.clickable { }
            )
        }

        Spacer(Modifier.height(12.dp))

        // ---------- LISTA DE DESTINOS ----------
        when (state) {

            is HomeUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator(color = PurpleMayu) }
            }

            is HomeUiState.Error -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = (state as HomeUiState.Error).message,
                        color = RedMayu
                    )
                    Spacer(Modifier.height(16.dp))
                    Button(onClick = { homeVM.refresh() }) {
                        Text("Reintentar", color = TextBlack)
                    }
                }
            }

            is HomeUiState.Success -> {
                val places = (state as HomeUiState.Success).places

                LazyRow(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                    items(places) { place ->
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

@Composable
fun DestinationCard(place: PlaceDto, onClick: () -> Unit) {
    val isBookmarked = remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier
            .width(280.dp)
            .height(380.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Box {
            Column {

                AsyncImage(
                    model = place.imageUrl,
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
                            fontWeight = FontWeight.Bold,
                            color = TextBlack
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(Modifier.height(6.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = android.R.drawable.ic_menu_mylocation),
                            contentDescription = null,
                            tint = GrayText,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(place.city, color = GrayText)
                    }

                    Spacer(Modifier.height(12.dp))

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = BeigeMayu
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
                                color = YellowMayu
                            )
                        }
                    }
                }
            }

            IconButton(
                onClick = { isBookmarked.value = !isBookmarked.value },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp)
                    .size(36.dp)
                    .background(WhiteBackground.copy(alpha = 0.9f), CircleShape)
            ) {
                Icon(
                    imageVector = if (isBookmarked.value) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                    contentDescription = "Bookmark",
                    tint = if (isBookmarked.value) RedMayu else GrayText
                )
            }
        }
    }

}
