package com.turismoapp.mayuandino.feature.login.presentation

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Facebook
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel

// Importa colores oficiales Mayu
import com.turismoapp.mayuandino.ui.theme.PurpleMayu
import com.turismoapp.mayuandino.ui.theme.GrayText
import com.turismoapp.mayuandino.ui.theme.TextBlack
import com.turismoapp.mayuandino.ui.theme.WhiteBackground

// Importaciones ViewModel
import com.turismoapp.mayuandino.feature.login.data.repository.AuthRepository
import com.turismoapp.mayuandino.feature.login.domain.usecase.SignInUseCase
import com.turismoapp.mayuandino.feature.login.domain.usecase.SignUpUseCase
import com.turismoapp.mayuandino.feature.login.domain.usecase.ValidateEmailUseCase
import com.turismoapp.mayuandino.feature.login.domain.usecase.ValidatePasswordUseCase
import com.google.firebase.auth.FirebaseAuth


class LoginViewModelFactory(
    private val signInUseCase: SignInUseCase,
    private val signUpUseCase: SignUpUseCase,
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase
) : androidx.lifecycle.ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(
                signInUseCase = signInUseCase,
                signUpUseCase = signUpUseCase,
                validateEmailUseCase = validateEmailUseCase,
                validatePasswordUseCase = validatePasswordUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

@Composable
fun getLoginViewModel(): LoginViewModel {
    val firebaseAuth = FirebaseAuth.getInstance()
    val authRepository = AuthRepository(firebaseAuth)
    val validateEmailUseCase = ValidateEmailUseCase()
    val validatePasswordUseCase = ValidatePasswordUseCase()
    val signInUseCase = SignInUseCase(authRepository)
    val signUpUseCase = SignUpUseCase(authRepository, validateEmailUseCase, validatePasswordUseCase)

    val factory = LoginViewModelFactory(signInUseCase, signUpUseCase, validateEmailUseCase, validatePasswordUseCase)
    return viewModel(factory = factory)
}


@Composable
fun LoginScreen(
    onSuccess: () -> Unit,
    onRegisterClick: () -> Unit = {},
    viewModel: LoginViewModel = getLoginViewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPass by remember { mutableStateOf(false) }

    val state by viewModel.loginState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(state) {
        when (val s = state) {
            is LoginState.Successful -> {
                onSuccess()
                viewModel.reset()
            }
            is LoginState.Error ->
                Toast.makeText(context, s.message, Toast.LENGTH_LONG).show()
            LoginState.Init,
            LoginState.Loading -> Unit
        }
    }

    val isLoading = state is LoginState.Loading

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = WhiteBackground
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "Bienvenido de nuevo",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = TextBlack
            )

            Text(
                text = "Por favor inicia sesión para acceder a nuestra app",
                fontSize = 15.sp,
                color = GrayText,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo electrónico") },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                trailingIcon = {
                    IconButton(onClick = { showPass = !showPass }) {
                        Icon(
                            imageVector = if (showPass) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                            contentDescription = null
                        )
                    }
                },
                visualTransformation = if (showPass) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = "¿Olvidaste tu contraseña?",
                color = PurpleMayu,
                fontSize = 14.sp,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(top = 8.dp)
            )

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = { viewModel.doLogin(email, password) },
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(containerColor = PurpleMayu),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("Entrando…", color = Color.White)
                } else {
                    Text("Iniciar Sesión", color = Color.White, fontSize = 16.sp)
                }
            }

            Spacer(Modifier.height(20.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("¿Aún no tienes una cuenta? ", color = GrayText)
                Text(
                    "Regístrate aquí",
                    color = PurpleMayu,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable { onRegisterClick() }
                )
            }

            Spacer(Modifier.height(30.dp))

            Text("o conéctate con:", color = GrayText, fontSize = 13.sp)

            Spacer(Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                Icon(Icons.Filled.Email, null, modifier = Modifier.size(36.dp), tint = GrayText)
                Icon(Icons.Filled.AccountCircle, null, modifier = Modifier.size(36.dp), tint = GrayText)
                Icon(Icons.Filled.Facebook, null, modifier = Modifier.size(36.dp), tint = GrayText)
            }
        }
    }
}
