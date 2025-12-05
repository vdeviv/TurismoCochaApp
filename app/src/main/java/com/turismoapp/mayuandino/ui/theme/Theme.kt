package com.turismoapp.mayuandino.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = PurpleMayu,
    secondary = YellowMayu,
    tertiary = GreenMayu,
    background = TextBlack,
    surface = TextBlack,
    onPrimary = WhiteBackground,
    onSecondary = WhiteBackground
)

private val LightColorScheme = lightColorScheme(
    primary = PurpleMayu,
    secondary = YellowMayu,
    tertiary = GreenMayu,
    background = WhiteBackground,
    surface = WhiteBackground,
    onPrimary = WhiteBackground,
    onSecondary = TextBlack,
    onTertiary = TextBlack
)

@Composable
fun TurismoAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
