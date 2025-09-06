package com.example.turismoapp.feature.login.domain.usecase

private val EMAIL_REGEX =
    Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")

class ValidateEmailUseCase {
    operator fun invoke(email: String): String? {
        val e = email.trim()
        if (e.isEmpty()) return "Ingresa tu correo"
        if (!EMAIL_REGEX.matches(e)) return "Correo inválido"
        return null
    }
}
