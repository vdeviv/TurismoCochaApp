package com.example.turismoapp.feature.onboarding.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.*
import com.example.turismoapp.R
import com.example.turismoapp.ui.theme.GreenMayu
import com.example.turismoapp.ui.theme.RedMayu
import com.example.turismoapp.ui.theme.YellowMayu
import kotlinx.coroutines.launch

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
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .systemBarsPadding()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // “Saltar” en texto plano (no botón)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = "Saltar",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable { onSkip() }
                )
            }

            // Páginas
            HorizontalPager(
                count = pages.size,
                state = pagerState,
                modifier = Modifier.weight(1f)
            ) { page ->
                OnboardingPageItem(page = pages[page])
            }

            // Indicadores
            HorizontalPagerIndicator(
                pagerState = pagerState,
                activeColor = MaterialTheme.colorScheme.primary,
                inactiveColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = 20.dp)
            )

            // Botón inferior
            Button(
                onClick = {
                    scope.launch {
                        if (pagerState.currentPage < pages.lastIndex) {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        } else {
                            onFinish()
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = when (pagerState.currentPage) {
                        0 -> RedMayu
                        1 -> YellowMayu
                        else -> GreenMayu
                    },
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .padding(horizontal = 24.dp, vertical = 10.dp)
            ) {
                Text(
                    text = if (pagerState.currentPage == pages.lastIndex) "Empezar" else "Siguiente",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
