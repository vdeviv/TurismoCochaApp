package com.turismoapp.mayuandino

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.google.firebase.FirebaseApp
import com.turismoapp.mayuandino.feature.navigation.AppNavigation
import com.turismoapp.mayuandino.ui.theme.TurismoAppTheme

class MainActivity : ComponentActivity() {

    // launcher para múltiples permisos
    private val multiplePermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.forEach { (permiso, granted) ->
                Log.d("Permisos", "$permiso = $granted")
            }
            launchApp()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        try {
            FirebaseApp.initializeApp(this)
            Log.d("Firebase", "Firebase inicializado correctamente")
        } catch (e: Exception) {
            Log.e("Firebase", "Error al inicializar Firebase: ${e.message}")
        }

        checkAndRequestPermissions()
    }

    private fun checkAndRequestPermissions() {

        val permissionsNeeded = mutableListOf<String>()

        // 🔹 UBICACIÓN (obligatoria si está en el Manifest)
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            permissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            permissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        }

        // 🔹 NOTIFICACIONES (Android 13+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED
            ) {
                permissionsNeeded.add(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        // ¿Faltan permisos?
        if (permissionsNeeded.isNotEmpty()) {
            multiplePermissionLauncher.launch(permissionsNeeded.toTypedArray())
        } else {
            launchApp()
        }
    }

    private fun launchApp() {
        setContent {
            TurismoAppTheme(darkTheme = false, dynamicColor = false) {
                AppNavigation()
            }
        }
    }
}
