package com.example.turismoapp.feature.login.domain.usecase

class ValidatePasswordUseCase {
    operator fun invoke(password: String): String? {
        // Validación de campo vacío
        if (password.isBlank()) {
            return "Ingresa tu contraseña"
        }

        // Validación de longitud mínima
        if (password.length < 6) {
            return "Mínimo 6 caracteres"
        }

        // Validación de mayúsculas (opcional pero recomendada)
        if (!password.any { it.isUpperCase() }) {
            return "Debe contener al menos una mayúscula"
        }

        // Validación de minúsculas
        if (!password.any { it.isLowerCase() }) {
            return "Debe contener al menos una minúscula"
        }

        // Validación de números
        if (!password.any { it.isDigit() }) {
            return "Debe contener al menos un número"
        }

        // Opcional: Validar caracteres especiales
        // if (!password.any { !it.isLetterOrDigit() }) {
        //     return "Debe contener al menos un carácter especial"
        // }

        return null // Contraseña válida
    }

    /**
     * Método adicional para validar fortaleza de la contraseña
     */
    fun getPasswordStrength(password: String): PasswordStrength {
        var score = 0

        if (password.length >= 6) score++
        if (password.length >= 8) score++
        if (password.any { it.isUpperCase() }) score++
        if (password.any { it.isLowerCase() }) score++
        if (password.any { it.isDigit() }) score++
        if (password.any { !it.isLetterOrDigit() }) score++

        return when {
            score <= 2 -> PasswordStrength.WEAK
            score <= 4 -> PasswordStrength.MEDIUM
            score <= 5 -> PasswordStrength.STRONG
            else -> PasswordStrength.VERY_STRONG
        }
    }
}

enum class PasswordStrength {
    WEAK,
    MEDIUM,
    STRONG,
    VERY_STRONG
}