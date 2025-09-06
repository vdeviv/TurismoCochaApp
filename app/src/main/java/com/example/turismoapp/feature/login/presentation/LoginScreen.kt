package com.example.turismoapp.feature.login.presentation

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.turismoapp.R

@Composable
fun LoginScreen(
    onSuccess: () -> Unit,
    viewModel: LoginViewModel = viewModel() // ← sin Hilt
) {
    var username by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    val state by viewModel.loginState.collectAsState()
    val context = LocalContext.current

    // Reaccionar a los cambios de estado (evita múltiples toasts)
    LaunchedEffect(state) {
        when (val s = state) {
            is LoginViewModel.LoginState.Successful -> onSuccess()
            is LoginViewModel.LoginState.Error ->
                Toast.makeText(context, s.message, Toast.LENGTH_LONG).show()
            LoginViewModel.LoginState.Init,
            LoginViewModel.LoginState.Loading -> Unit
        }
    }

    Scaffold { inner ->
        Box(
            modifier = Modifier
                .padding(inner)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = null,
                    modifier = Modifier.size(72.dp)
                )

                Spacer(Modifier.height(12.dp))
                Text(text = stringResource(id = R.string.login_title))
                Spacer(Modifier.height(16.dp))

                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text(stringResource(R.string.username)) },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text(stringResource(R.string.password)) },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(16.dp))

                OutlinedButton(
                    onClick = { viewModel.doLogin(username, password) },
                    enabled = state !is LoginViewModel.LoginState.Loading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text(text = stringResource(R.string.login))
                }
            }
        }
    }
}
