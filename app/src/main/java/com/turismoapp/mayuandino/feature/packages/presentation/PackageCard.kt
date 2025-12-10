package com.turismoapp.mayuandino.feature.packages.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.turismoapp.mayuandino.R
import com.turismoapp.mayuandino.feature.packages.domain.model.PackageModel
import com.turismoapp.mayuandino.ui.theme.GrayText
import com.turismoapp.mayuandino.ui.theme.PurpleMayu
import com.turismoapp.mayuandino.ui.theme.TextBlack
import com.turismoapp.mayuandino.ui.theme.WhiteBackground

@Composable
fun PackageCard(
    pkg: PackageModel,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = WhiteBackground),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {

        Row(
            modifier = Modifier
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // ---------------- IMAGEN GRANDE (Figma Style) ----------------
            AsyncImage(
                model = pkg.imageUrl,
                contentDescription = pkg.title,
                modifier = Modifier
                    .size(width = 110.dp, height = 100.dp)
                    .clip(RoundedCornerShape(14.dp)),
                contentScale = ContentScale.Crop
            )

            // ---------------- INFORMACIÓN ----------------
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {

                Text(
                    pkg.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = TextBlack
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    pkg.city,
                    style = MaterialTheme.typography.bodySmall,
                    color = GrayText
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    "⭐ ${pkg.rating}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFFFFC107)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // ---------------- AVATARES + TEXTO ----------------
                Row(verticalAlignment = Alignment.CenterVertically) {

                    val participants = pkg.joinedUsers

                    // Solo mostramos hasta 3 avatares superpuestos
                    if (participants.isNotEmpty()) {

                        participants.take(3).forEachIndexed { index, url ->

                            AsyncImage(
                                model = url,
                                contentDescription = "avatar",
                                modifier = Modifier
                                    .size(22.dp)
                                    .clip(CircleShape)
                                    .offset(x = (-6 * index).dp),
                                contentScale = ContentScale.Crop
                            )
                        }

                        Spacer(modifier = Modifier.width(6.dp))

                    }

                    Text(
                        "${participants.size} personas se unieron",
                        style = MaterialTheme.typography.bodySmall,
                        color = GrayText
                    )
                }
            }

            // ---------------- BOTÓN VER ----------------
            Button(
                onClick = onClick,
                colors = ButtonDefaults.buttonColors(containerColor = PurpleMayu),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .align(Alignment.CenterVertically)
            ) {
                Text("Ver", color = Color.White)
            }
        }
    }
}
