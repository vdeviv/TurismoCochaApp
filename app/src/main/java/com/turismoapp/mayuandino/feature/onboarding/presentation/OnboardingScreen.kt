package com.turismoapp.mayuandino.feature.onboarding.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.*
import com.turismoapp.mayuandino.ui.theme.GreenMayu
import com.turismoapp.mayuandino.ui.theme.RedMayu
import com.turismoapp.mayuandino.ui.theme.YellowMayu
import kotlinx.coroutines.launch
import com.turismoapp.mayuandino.R


@OptIn(ExperimentalPagerApi::class)
@Composable
fun OnboardingScreen(
    onSkip: () -> Unit,
    onFinish: () -> Unit
) {
    // Definición de páginas (Se mantiene igual)
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
            .background(Color.White)
            .navigationBarsPadding()
            .imePadding()
    ) {
        // Páginas (Se mantiene igual)
        HorizontalPager(
            count = pages.size,
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            OnboardingPageItem(page = pages[page])
        }

        // Texto "Saltar" (Se mantiene igual)
        Text(
            text = "Saltar",
            color = MaterialTheme.colorScheme.primary, // Asumiendo que el color principal es el rojo del mockup
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 40.dp, end = 24.dp)
                .clickable { onSkip() }
        )

        // Indicadores (Se mantiene igual)
        HorizontalPagerIndicator(
            pagerState = pagerState,
            activeColor = MaterialTheme.colorScheme.primary,
            inactiveColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 120.dp)
        )

        // *** BOTÓN CORREGIDO PARA EL MOCKUP ***
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
                // Color dinámico según la página
                containerColor = when (pagerState.currentPage) {
                    0 -> RedMayu
                    1 -> YellowMayu
                    else -> GreenMayu
                },
                contentColor = Color.White // Aseguramos que el contenido (Texto) sea blanco
            ),
            // Forma y elevación como en el mockup
            shape = RoundedCornerShape(20.dp),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 4.dp,
                pressedElevation = 6.dp
            ),
            // Posicionamiento
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(0.85f) // Ajustado ligeramente a 85% para un mejor margen lateral (el 0.9f original también funcionaba)
                .height(56.dp)
                .padding(bottom = 40.dp)
            // Se elimina navigationBarsPadding aquí ya que se aplicó al Box contenedor
        ) {
            Text(
                // Texto dinámico: "Empezar" en la última página, "Siguiente" en las demás
                text = if (pagerState.currentPage == pages.lastIndex) "Empezar" else "Siguiente",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White, // Aseguramos el color blanco para el texto
                letterSpacing = 0.5.sp,
            )
        }
    }
}