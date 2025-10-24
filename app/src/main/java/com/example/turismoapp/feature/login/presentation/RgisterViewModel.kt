
package com.example.turismoapp.feature.login.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.turismoapp.feature.login.data.repository.AuthRepository
import com.example.turismoapp.feature.login.domain.usecase.SignUpCase
import com.example.turismoapp.feature.login.domain.usecase.ValidateEmailUseCase
import com.example.turismoapp.feature.login.domain.usecase.ValidatePasswordUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val signUp: SignUpCase = SignUpCase(AuthRepository()),
    private val validateEmail: ValidateEmailUseCase = ValidateEmailUseCase(),
    private val validatePassword: ValidatePasswordUseCase = ValidatePasswordUseCase()
) : ViewModel() {

    sealed interface RegisterState {
        data object Init : RegisterState
        data object Loading : RegisterState
        data object Successful : RegisterState
        data class Error(val message: String) : RegisterState
    }

    private val _registerState = MutableStateFlow<RegisterState>(RegisterState.Init)
    val registerState: StateFlow<RegisterState> = _registerState.asStateFlow()

    fun doRegister(email: String, pass: String) {
        viewModelScope.launch {
            _registerState.value = RegisterState.Loading

            // 1. Validación de campos
            validateEmail(email)?.let { msg -> _registerState.value = RegisterState.Error(msg); return@launch }
            validatePassword(pass)?.let { msg -> _registerState.value = RegisterState.Error(msg); return@launch }

            // 2. Ejecutar el caso de uso
            val result = signUp(email, pass)
            _registerState.value = result.fold(
                onSuccess = { RegisterState.Successful },
                onFailure = { RegisterState.Error(it.message ?: "Error inesperado al registrar") }
            )
        }
    }

    fun reset() { _registerState.value = RegisterState.Init }
}