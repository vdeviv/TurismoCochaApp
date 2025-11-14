package com.example.turismoapp.feature.login.domain.usecase

class ValidateEmailUseCase {
    operator fun invoke(email: String): String? {
        if (email.isBlank()) {
            return "El email no puede estar vacío."
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return "Formato de email inválido."
        }
        return null // Éxito
    }
}