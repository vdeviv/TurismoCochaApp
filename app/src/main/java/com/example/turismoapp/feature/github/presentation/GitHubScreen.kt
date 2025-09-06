package com.example.turismoapp.feature.github.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
fun GitHubScreen( modifier: Modifier) {
    var nickname by remember { mutableStateOf("") }

    Column {
        Text(text = "")

        OutlinedTextField(
            value = nickname,
            onValueChange = {
                nickname = it
            }
        )

        OutlinedButton(onClick = {
            // Acción al presionar el botón
        }) {
            Text(text = "")
        }
    }
}
