package com.example.turismoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.turismoapp.di.appModule
import com.example.turismoapp.feature.navigation.AppNavigation
import com.example.turismoapp.ui.theme.TurismoAppTheme
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializa Koin
        startKoin {
            androidContext(this@MainActivity) // Pasa el contexto de la actividad
            modules(appModule) // Registra el módulo de Koin que contiene tus dependencias
        }

        enableEdgeToEdge()
        setContent {
            TurismoAppTheme {
                AppNavigation() // Comienza la navegación de la app
            }
        }
    }
}
