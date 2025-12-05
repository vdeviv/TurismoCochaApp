package com.turismoapp.mayuandino.feature.login.presentation

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val emailError: String? = null,
    val passwordError: String? = null,
    val loading: Boolean = false,
    val errorMessage: String? = null
)