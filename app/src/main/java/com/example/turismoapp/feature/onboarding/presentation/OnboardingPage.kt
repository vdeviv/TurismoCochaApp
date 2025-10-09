package com.example.turismoapp.feature.onboarding.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

data class OnboardingPageModel(
    val imageRes: Int,
    val title: String,
    val description: String
)

@Composable
fun OnboardingPageItem(
    page: OnboardingPageModel,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = page.imageRes),
            contentDescription = page.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(16.dp)
                .height(260.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
        )
        Spacer(Modifier.height(12.dp))
        Text(page.title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Text(page.description, style = MaterialTheme.typography.bodyMedium)
    }
}
