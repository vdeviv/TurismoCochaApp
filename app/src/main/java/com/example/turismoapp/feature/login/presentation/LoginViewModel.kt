package com.example.turismoapp.feature.login.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

import com.example.turismoapp.feature.login.data.repository.AuthRepository
import com.example.turismoapp.feature.login.domain.usecase.SignInUseCase
import com.example.turismoapp.feature.login.domain.usecase.ValidateEmailUseCase
import com.example.turismoapp.feature.login.domain.usecase.ValidatePasswordUseCase

class LoginViewModel(
    private val signIn: SignInUseCase = SignInUseCase(AuthRepository()),
    private val validateEmail: ValidateEmailUseCase = ValidateEmailUseCase(),
    private val validatePassword: ValidatePasswordUseCase = ValidatePasswordUseCase()
) : ViewModel() {

    sealed interface LoginState {
        data object Init : LoginState
        data object Loading : LoginState
        data object Successful : LoginState
        data class Error(val message: String) : LoginState
    }

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Init)
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()



    fun doLogin(user: String, pass: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading

            // Validación (opcional, puedes dejarla en la UI)
            validateEmail(user)?.let { msg -> _loginState.value = LoginState.Error(msg); return@launch }
            validatePassword(pass)?.let { msg -> _loginState.value = LoginState.Error(msg); return@launch }

            val result = signIn(user, pass)
            _loginState.value = result.fold(
                onSuccess = { LoginState.Successful },
                onFailure = { LoginState.Error(it.message ?: "Error inesperado") }
            )
        }
    }


    fun reset() { _loginState.value = LoginState.Init }
}
