package com.example.turismoapp.feature.login.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.turismoapp.feature.login.domain.model.AuthUser
import com.example.turismoapp.feature.login.domain.model.Result
import com.example.turismoapp.feature.login.domain.usecase.SignInUseCase
import com.example.turismoapp.feature.login.domain.usecase.SignUpUseCase // Usamos SignUpUseCase
import com.example.turismoapp.feature.login.domain.usecase.ValidateEmailUseCase
import com.example.turismoapp.feature.login.domain.usecase.ValidatePasswordUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Clase sellada para manejar el estado completo de la pantalla de login (o ambas, login/registro)
sealed class LoginState {
    object Init : LoginState()
    object Loading : LoginState()
    data class Successful(val user: AuthUser) : LoginState()
    data class Error(val message: String) : LoginState()
}

/**
 * ViewModel que maneja la lógica de la pantalla de Login.
 */
class LoginViewModel(
    private val signInUseCase: SignInUseCase,
    private val signUpUseCase: SignUpUseCase, // Renombrado a SignUpUseCase
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase
) : ViewModel() {

    // Estado observable que la UI consumirá (Usando LoginState)
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Init)
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    fun reset() {
        _loginState.value = LoginState.Init
    }

    /**
     * Lógica para el INICIO DE SESIÓN.
     */
    fun doLogin(email: String, password: String) {
        // 1. Validaciones básicas antes de llamar al Use Case (opcional, pero buena práctica)
        val emailError = validateEmailUseCase(email)
        val passwordError = validatePasswordUseCase(password)

        if (emailError != null || passwordError != null) {
            _loginState.update { LoginState.Error(emailError ?: passwordError!!) }
            return
        }

        // 2. Ejecutar Use Case
        viewModelScope.launch {
            _loginState.value = LoginState.Loading

            when (val result = signInUseCase(email, password)) {
                is Result.Success -> {
                    _loginState.value = LoginState.Successful(result.data)
                }
                is Result.Error -> {
                    _loginState.value = LoginState.Error(result.exception.message ?: "Error desconocido")
                }
                is Result.Loading -> Unit // No utilizado en la arquitectura actual
            }
        }
    }

    /**
     * Lógica para el REGISTRO DE USUARIO (Si decides reutilizar este ViewModel)
     */
    fun doSignUp(email: String, password: String) {
        // Validación de campos
        val emailError = validateEmailUseCase(email)
        val passwordError = validatePasswordUseCase(password)

        if (emailError != null || passwordError != null) {
            _loginState.update { LoginState.Error(emailError ?: passwordError!!) }
            return
        }

        viewModelScope.launch {
            _loginState.value = LoginState.Loading

            when (val result = signUpUseCase(email, password)) {
                is Result.Success -> {
                    // Si el registro es exitoso, se considera un inicio de sesión
                    _loginState.value = LoginState.Successful(result.data)
                }
                is Result.Error -> {
                    _loginState.value = LoginState.Error(result.exception.message ?: "Error de registro desconocido")
                }
                is Result.Loading -> Unit
            }
        }
    }
}