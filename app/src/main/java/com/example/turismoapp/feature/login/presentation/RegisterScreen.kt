package com.example.turismoapp.feature.login.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.turismoapp.feature.login.data.repository.AuthRepository
import com.example.turismoapp.feature.login.domain.usecase.SignUpUseCase
import com.example.turismoapp.feature.login.domain.usecase.ValidateEmailUseCase
import com.example.turismoapp.feature.login.domain.usecase.ValidatePasswordUseCase // Importamos el UseCase
import com.google.firebase.auth.FirebaseAuth

// Clase de inyección manual simple para el ViewModel (reemplazar con Hilt/Koin en producción)
class RegisterViewModelFactory(
    private val signUpUseCase: SignUpUseCase,
    private val validateEmailUseCase: ValidateEmailUseCase
) : androidx.lifecycle.ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(
                signUpUseCase = signUpUseCase,
                validateEmailUseCase = validateEmailUseCase,
                // Referencia simplificada al UseCase
                validatePasswordUseCase = ValidatePasswordUseCase()
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

// Configuración de la inyección de dependencias para el ejemplo
@Composable
fun getRegisterViewModel(): RegisterViewModel {
    val firebaseAuth = FirebaseAuth.getInstance()
    val authRepository = AuthRepository(firebaseAuth)
    val validateEmailUseCase = ValidateEmailUseCase()
    val validatePasswordUseCase = ValidatePasswordUseCase() // Referencia simple
    val signUpUseCase = SignUpUseCase(authRepository, validateEmailUseCase, validatePasswordUseCase)

    val factory = RegisterViewModelFactory(signUpUseCase, validateEmailUseCase)
    return viewModel(factory = factory)
}

// Añadimos la anotación @OptIn para permitir el uso de TopAppBar sin la advertencia
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = getRegisterViewModel(),
    onRegistrationSuccess: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    // Manejar la navegación después del éxito
    if (uiState.success) {
        onRegistrationSuccess()
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Registro de Usuario") }) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp), // Aplicar padding horizontal aquí
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // Campo de Email
            OutlinedTextField(
                value = uiState.formState.email,
                onValueChange = { viewModel.onEvent(RegisterEvent.EmailChanged(it)) },
                label = { Text("Correo Electrónico") },
                isError = uiState.emailError != null,
                supportingText = {
                    if (uiState.emailError != null) {
                        Text(uiState.emailError!!)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Campo de Contraseña
            OutlinedTextField(
                value = uiState.formState.password,
                onValueChange = { viewModel.onEvent(RegisterEvent.PasswordChanged(it)) },
                label = { Text("Contraseña") },
                isError = uiState.passwordError != null,
                supportingText = {
                    if (uiState.passwordError != null) {
                        Text(uiState.passwordError!!)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Campo de Confirmación de Contraseña
            OutlinedTextField(
                value = uiState.formState.passwordConfirm,
                onValueChange = { viewModel.onEvent(RegisterEvent.PasswordConfirmChanged(it)) },
                label = { Text("Confirmar Contraseña") },
                isError = uiState.passwordConfirmError != null,
                supportingText = {
                    if (uiState.passwordConfirmError != null) {
                        Text(uiState.passwordConfirmError!!)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(24.dp))

            // Botón de Registro
            Button(
                onClick = { viewModel.onEvent(RegisterEvent.Submit) },
                enabled = !uiState.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(24.dp))
                } else {
                    Text("Registrarse")
                }
            }

            // Mostrar mensaje de error general
            uiState.error?.let {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Error: $it",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            // Mostrar mensaje de éxito
            if (uiState.success) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "¡Registro exitoso! Redirigiendo...",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}