package com.example.turismoapp.feature.login.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

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
            // TODO: reemplazar por tu llamada real (repositorio/endpoint)
            delay(600)
            _loginState.value =
                if (user.trim() == "admin" && pass == "1234")
                    LoginState.Successful
                else
                    LoginState.Error("Credenciales inválidas")
        }
    }

    fun reset() { _loginState.value = LoginState.Init }
}
