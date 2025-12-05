package com.turismoapp.mayuandino

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.turismoapp.mayuandino.feature.navigation.AppNavigation
import com.turismoapp.mayuandino.ui.theme.TurismoAppTheme
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        try {
            FirebaseApp.initializeApp(this)
            Log.d("Firebase", " Firebase inicializado correctamente")
        } catch (e: Exception) {
            Log.e("Firebase", " Error al inicializar Firebase: ${e.message}")
        }

        setContent {
            TurismoAppTheme(darkTheme = false, dynamicColor = false) {
                AppNavigation()
            }
        }
    }
}
