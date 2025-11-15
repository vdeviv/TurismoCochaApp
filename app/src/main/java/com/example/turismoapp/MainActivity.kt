package com.example.turismoapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.turismoapp.feature.navigation.AppNavigation
import com.example.turismoapp.ui.theme.TurismoAppTheme
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            TurismoAppTheme(darkTheme = false, dynamicColor = false) {
                AppNavigation()
            }
        }
        try {
            FirebaseApp.initializeApp(this)
            Log.d("Firebase", "Firebase inicializado correctamente")
        } catch (e: Exception) {
            Log.e("Firebase", "Error al inicializar Firebase: ${e.message}")
        }
    }
}
