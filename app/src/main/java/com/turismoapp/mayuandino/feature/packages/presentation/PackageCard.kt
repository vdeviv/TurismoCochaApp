package com.turismoapp.mayuandino.feature.packages.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.turismoapp.mayuandino.feature.packages.domain.model.PackageModel

@Composable
fun PackageCard(
    pkg: PackageModel,
    onClick: (String) -> Unit   // <--- NECESARIO
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(Modifier.padding(12.dp)) {

            AsyncImage(
                model = pkg.imageUrl,
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(Modifier.width(12.dp))

            Column(Modifier.weight(1f)) {
                Text(pkg.title, style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(6.dp))
                Text(pkg.city, style = MaterialTheme.typography.bodySmall)
                Spacer(Modifier.height(6.dp))
                Text("⭐ ${pkg.rating}", style = MaterialTheme.typography.bodyMedium)
            }

            Button(
                onClick = { onClick(pkg.id) },   // <--- NAVEGA AL DETALLE
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF772CE8)
                )
            ) {
                Text("Ver")
            }
        }
    }
}
