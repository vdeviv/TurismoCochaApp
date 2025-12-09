package com.turismoapp.mayuandino

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.content.ContextCompat
import com.turismoapp.mayuandino.feature.navigation.AppNavigation
import com.turismoapp.mayuandino.ui.theme.TurismoAppTheme
import com.google.firebase.FirebaseApp
import com.google.firebase.Firebase
import android.Manifest
import androidx.activity.result.contract.ActivityResultContracts

class MainActivity : ComponentActivity() {
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                // Permiso concedido. Puedes iniciar operaciones de notificación aquí.
                Log.d("Permisos", "Permiso de notificaciones concedido")
            } else {
                // Permiso denegado. Informa al usuario o deshabilita la funcionalidad.
                Log.d("Permisos", "Permiso de notificaciones denegado")
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        try {
            FirebaseApp.initializeApp(this)
            Log.d("Firebase", " Firebase inicializado correctamente")
        } catch (e: Exception) {
            Log.e("Firebase", " Error al inicializar Firebase: ${e.message}")
        }
        askNotificationPermission()

        setContent {
            TurismoAppTheme(darkTheme = false, dynamicColor = false) {
                AppNavigation()
            }
        }
    }

    private fun askNotificationPermission() {
        // Solo es necesario en Android 13 (API 33) o superior
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // Ya tenemos permiso
            } else {
                // Pedimos permiso
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}
