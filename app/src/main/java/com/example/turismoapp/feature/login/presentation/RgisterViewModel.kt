package com.example.turismoapp.feature.login.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.turismoapp.feature.login.domain.model.Result
import com.example.turismoapp.feature.login.domain.usecase.SignUpUseCase
import com.example.turismoapp.feature.login.domain.usecase.ValidateEmailUseCase
import com.example.turismoapp.feature.login.domain.usecase.ValidatePasswordUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Nota: En una app real usarías Hilt o Koin para inyectar estas dependencias.
// Aquí las pasamos por el constructor.
class RegisterViewModel(
    private val signUpUseCase: SignUpUseCase,
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase // Para validar en tiempo real si quieres
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState

    fun onEvent(event: RegisterEvent) {
        when (event) {
            is RegisterEvent.EmailChanged -> {
                _uiState.update {
                    it.copy(
                        formState = it.formState.copy(email = event.email),
                        emailError = null, // Limpiar error al escribir
                        error = null
                    )
                }
            }
            is RegisterEvent.PasswordChanged -> {
                _uiState.update {
                    it.copy(
                        formState = it.formState.copy(password = event.password),
                        passwordError = null,
                        error = null
                    )
                }
            }
            is RegisterEvent.PasswordConfirmChanged -> {
                _uiState.update {
                    it.copy(
                        formState = it.formState.copy(passwordConfirm = event.passwordConfirm),
                        passwordConfirmError = null,
                        error = null
                    )
                }
            }
            RegisterEvent.Submit -> submitRegistration()
        }
    }

    private fun submitRegistration() {
        val form = _uiState.value.formState

        // Validaciones en UI/ViewModel antes de llamar al Caso de Uso
        val emailError = validateEmailUseCase(form.email)
        val passwordError = validatePasswordUseCase(form.password)
        val passwordConfirmError = if (form.password != form.passwordConfirm) {
            "Las contraseñas no coinciden."
        } else if (form.passwordConfirm.isBlank()) {
            "Confirma tu contraseña."
        } else {
            null
        }

        if (emailError != null || passwordError != null || passwordConfirmError != null) {
            _uiState.update {
                it.copy(
                    emailError = emailError,
                    passwordError = passwordError,
                    passwordConfirmError = passwordConfirmError
                )
            }
            return
        }

        // Si las validaciones pasan, iniciar el registro
        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            val result = signUpUseCase(form.email, form.password)

            _uiState.update { state ->
                when (result) {
                    is Result.Success -> state.copy(
                        isLoading = false,
                        success = true,
                        error = null
                    )
                    is Result.Error -> state.copy(
                        isLoading = false,
                        success = false,
                        error = result.exception.message
                    )
                    is Result.Loading -> state.copy(isLoading = true) // No debería pasar aquí
                }
            }
        }
    }
}

// Eventos para el ViewModel
sealed class RegisterEvent {
    data class EmailChanged(val email: String) : RegisterEvent()
    data class PasswordChanged(val password: String) : RegisterEvent()
    data class PasswordConfirmChanged(val passwordConfirm: String) : RegisterEvent()
    object Submit : RegisterEvent()
}