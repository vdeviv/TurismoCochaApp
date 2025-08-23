package com.example.turismoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.turismoapp.presentation.GitHubScreen
import com.example.turismoapp.ui.theme.TurismoAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TurismoAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    GitHubScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}
