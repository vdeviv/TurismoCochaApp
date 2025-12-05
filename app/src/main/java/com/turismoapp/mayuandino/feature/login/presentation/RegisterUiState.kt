package com.turismoapp.mayuandino.feature.login.presentation

data class RegisterFormState(
    val email: String = "",
    val password: String = "",
    val passwordConfirm: String = ""
)

// Estado general de la pantalla de registro
data class RegisterUiState(
    val formState: com.turismoapp.mayuandino.feature.login.presentation.RegisterFormState = RegisterFormState(),
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val error: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val passwordConfirmError: String? = null
)