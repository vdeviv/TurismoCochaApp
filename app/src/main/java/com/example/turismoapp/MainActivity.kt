package com.example.turismoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.turismoapp.feature.navigation.AppNavigation
import com.example.turismoapp.ui.theme.TurismoAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            TurismoAppTheme(darkTheme = false, dynamicColor = false) {
                AppNavigation()
            }
        }
    }
}
