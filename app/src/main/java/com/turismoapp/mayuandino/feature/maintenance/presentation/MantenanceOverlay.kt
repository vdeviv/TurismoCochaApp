package com.turismoapp.mayuandino.feature.maintenance.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.turismoapp.mayuandino.ui.theme.*

@Composable
fun MaintenanceOverlay() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BeigeMayu),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Icon(
            Icons.Default.Build,
            contentDescription = null,
            modifier = Modifier.size(120.dp),
            tint = PurpleMayu
        )

        Spacer(Modifier.height(24.dp))

        Text(
            text = "Estamos mejorando Mayu Andino",
            style = MaterialTheme.typography.headlineMedium,
            color = TextBlack
        )

        Spacer(Modifier.height(12.dp))

        Text(
            text = "Volveremos pronto con nuevas rutas, eventos y experiencias.",
            style = MaterialTheme.typography.bodyLarge,
            color = GrayText,
            modifier = Modifier.padding(horizontal = 32.dp)
        )
    }
}
