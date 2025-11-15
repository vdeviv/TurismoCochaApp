package com.example.turismoapp.feature.login.presentation

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

// *** Importaciones corregidas ***
import com.example.turismoapp.feature.login.data.repository.AuthRepository
import com.example.turismoapp.feature.login.domain.usecase.SignInUseCase
import com.example.turismoapp.feature.login.domain.usecase.SignUpUseCase
import com.example.turismoapp.feature.login.domain.usecase.ValidateEmailUseCase
import com.example.turismoapp.feature.login.domain.usecase.ValidatePasswordUseCase
import com.google.firebase.auth.FirebaseAuth
// ******************************


// Definición simple de colores (asumiendo que las tienes definidas en ui.theme)
val PurpleMayu = Color(0xFF673AB7) // Ejemplo
val GrayText = Color(0xFF888888) // Ejemplo


// Clase de inyección manual simple para el ViewModel (reemplazar con Hilt/Koin en producción)
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

// Configuración de la inyección de dependencias para el ejemplo
@Composable
fun getLoginViewModel(): LoginViewModel {
    val firebaseAuth = FirebaseAuth.getInstance()
    val authRepository = AuthRepository(firebaseAuth)
    val validateEmailUseCase = ValidateEmailUseCase()
    val validatePasswordUseCase = ValidatePasswordUseCase()
    val signInUseCase = SignInUseCase(authRepository)
    val signUpUseCase = SignUpUseCase(authRepository, validateEmailUseCase, validatePasswordUseCase) // Usamos SignUpUseCase aquí

    val factory = LoginViewModelFactory(signInUseCase, signUpUseCase, validateEmailUseCase, validatePasswordUseCase)
    return viewModel(factory = factory)
}


@Composable
fun LoginScreen(
    onSuccess: () -> Unit,
    onRegisterClick: () -> Unit = {},
    viewModel: LoginViewModel = getLoginViewModel() // Usar la función de inyección
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPass by remember { mutableStateOf(false) }

    val state by viewModel.loginState.collectAsState()
    val context = LocalContext.current

    // Efecto para manejar navegación y errores
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

    // Fondo blanco puro
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Título
            Text(
                text = "Bienvenido de nuevo",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            Text(
                text = "Por favor inicia sesión para acceder a nuestra app",
                fontSize = 15.sp,
                color = GrayText,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Campo email
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

            // Campo contraseña
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                trailingIcon = {
                    val icon = if (showPass) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
                    IconButton(onClick = { showPass = !showPass }) {
                        Icon(imageVector = icon, contentDescription = null)
                    }
                },
                visualTransformation = if (showPass) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                modifier = Modifier.fillMaxWidth()
            )

            // Enlace “Olvidaste tu contraseña”
            Text(
                text = "¿Olvidaste tu contraseña?",
                color = PurpleMayu,
                fontSize = 14.sp,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(top = 8.dp)
                    .clickable { /* Acción futura */ }
            )

            Spacer(Modifier.height(24.dp))

            // Botón de inicio de sesión
            Button(
                onClick = { viewModel.doLogin(email, password) }, // <<-- LLamada correcta al ViewModel
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(containerColor = PurpleMayu),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("Entrando…", color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text("Iniciar Sesión", color = MaterialTheme.colorScheme.onPrimary, fontSize = 16.sp)
                }
            }

            Spacer(Modifier.height(20.dp))

            // Registro
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("¿Aún no tienes una cuenta? ", color = GrayText, fontSize = 14.sp)
                Text(
                    "Regístrate aquí",
                    color = PurpleMayu,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable { onRegisterClick() } // <-- Usa el callback
                )
            }

            Spacer(Modifier.height(30.dp))

            // Iconos de redes sociales (placeholder)
            Text("o conéctate con:", color = GrayText, fontSize = 13.sp)

            Spacer(Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Email,
                    contentDescription = "Google",
                    modifier = Modifier.size(36.dp),
                    tint = Color.Gray
                )
                Icon(
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = "Facebook",
                    modifier = Modifier.size(36.dp),
                    tint = Color.Gray
                )
                Icon(
                    imageVector = Icons.Filled.Facebook,
                    contentDescription = "Instagram",
                    modifier = Modifier.size(36.dp),
                    tint = Color.Gray
                )
            }
        }
    }
}