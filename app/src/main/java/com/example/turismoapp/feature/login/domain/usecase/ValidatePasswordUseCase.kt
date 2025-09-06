package com.example.turismoapp.feature.login.domain.usecase

class ValidatePasswordUseCase {
    operator fun invoke(password: String): String? {
        if (password.isBlank()) return "Ingresa tu contraseña"
        if (password.length < 6) return "Mínimo 6 caracteres"
        return null
    }
}