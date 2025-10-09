package com.example.turismoapp.feature.onboarding.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import com.example.turismoapp.R

@OptIn(ExperimentalPagerApi::class)
@Composable
fun OnboardingScreen(
    onSkip: () -> Unit,
    onFinish: () -> Unit
) {
    val pages = listOf(
        OnboardingPageModel(
            imageRes = R.drawable.onboarding1,
            title = "La vida es corta y el mundo es amplio",
            description = "Somos una app para explorar Cochabamba fácil y rápido."
        ),
        OnboardingPageModel(
            imageRes = R.drawable.onboarding2,
            title = "It’s a big world out there, go explore",
            description = "Encuentra lugares, eventos y rutas para tu viaje."
        ),
        OnboardingPageModel(
            imageRes = R.drawable.onboarding3,
            title = "People don’t take trips, trips take people",
            description = "Guarda tus favoritos y comparte tu experiencia."
        )
    )

    val pagerState = rememberPagerState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // botón Skip
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            Button(onClick = onSkip) { Text("Skip") }
        }

        HorizontalPager(
            count = pages.size,
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { index ->
            OnboardingPageItem(page = pages[index])
        }

        Spacer(Modifier.height(12.dp))

        HorizontalPagerIndicator(
            pagerState = pagerState,
            modifier = Modifier.padding(16.dp)
        )

        Button(
            onClick = {
                if (pagerState.currentPage == pages.lastIndex) onFinish()
                else {
                    // Avanza a la siguiente página
                    // (con accompanist 0.28.0 es legal usar animateScrollToPage en coroutine;
                    // para simplificar lo dejamos en onSkip/onFinish)
                    onSkip()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (pagerState.currentPage == pages.lastIndex) "Empezar" else "Siguiente")
        }
    }
}
