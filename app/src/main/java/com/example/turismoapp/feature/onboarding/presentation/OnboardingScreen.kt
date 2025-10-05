package com.example.turismoapp.features.onboarding.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.*
import com.example.turismoapp.R

@OptIn(ExperimentalPagerApi::class)
@Composable
fun OnboardingScreen(
    onSkip: () -> Unit,
    onFinish: () -> Unit
) {
    val pagerState = rememberPagerState()

    val pages = listOf(
        Triple(R.drawable.onboarding1, "La vida es corta y el mundo es amplio", "Somos una aplicación creada para que viajar en Cochabamba sea más fácil y rápido."),
        Triple(R.drawable.onboarding2, "It’s a big world out there", "Explora nuevos destinos, vive nuevas experiencias y descubre lugares mágicos."),
        Triple(R.drawable.onboarding3, "People don’t take trips, trips take people", "Tu próxima aventura comienza aquí, con nosotros.")
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextButton(
            onClick = onSkip,
            modifier = Modifier.align(Alignment.End).padding(16.dp)
        ) { Text("Saltar") }

        HorizontalPager(count = pages.size, state = pagerState) { page ->
            val (image, title, desc) = pages[page]
            OnboardingPage(
                imageRes = image,
                title = title,
                description = desc,
                buttonText = if (page == pages.lastIndex) "Empezar" else "Siguiente",
                onButtonClick = {
                    if (page == pages.lastIndex) onFinish() else {
                        LaunchedEffect(Unit) {
                            pagerState.scrollToPage(page + 1)
                        }
                    }
                }
            )
        }

        HorizontalPagerIndicator(
            pagerState = pagerState,
            modifier = Modifier.padding(16.dp)
        )
    }
}
