package com.example.turismoapp.feature.login.domain.usecase

import android.util.Patterns

class ValidateEmailUseCase {
    operator fun invoke(email: String): String? {
        // Validación de campo vacío
        if (email.isBlank()) {
            return "El email no puede estar vacío"
        }

        // Eliminar espacios en blanco al inicio y final
        val trimmedEmail = email.trim()

        // Validación de longitud mínima razonable
        if (trimmedEmail.length < 5) {
            return "Email demasiado corto"
        }

        // Validación de formato usando el patrón de Android
        if (!Patterns.EMAIL_ADDRESS.matcher(trimmedEmail).matches()) {
            return "Formato de email inválido"
        }

        // Validación adicional: debe contener @ y punto después del @
        val atIndex = trimmedEmail.indexOf('@')
        if (atIndex == -1) {
            return "Debe contener el símbolo @"
        }

        val afterAt = trimmedEmail.substring(atIndex + 1)
        if (!afterAt.contains('.')) {
            return "Email debe tener dominio válido"
        }

        // Validación: no debe tener espacios
        if (trimmedEmail.contains(' ')) {
            return "El email no debe contener espacios"
        }

        // Validación: no debe empezar o terminar con punto
        if (trimmedEmail.startsWith('.') || trimmedEmail.endsWith('.')) {
            return "Email no puede empezar o terminar con punto"
        }

        // Validación: el dominio debe ser válido
        val domain = trimmedEmail.substringAfter('@')
        if (domain.length < 3) {
            return "Dominio de email inválido"
        }

        // Validación adicional: comprobar dominios comunes mal escritos
        val commonMisspellings = mapOf(
            "gmial.com" to "gmail.com",
            "gmai.com" to "gmail.com",
            "gnail.com" to "gmail.com",
            "hotmial.com" to "hotmail.com",
            "yahooo.com" to "yahoo.com",
            "outlok.com" to "outlook.com"
        )

        if (commonMisspellings.containsKey(domain)) {
            return "¿Quisiste decir ${commonMisspellings[domain]}?"
        }

        return null // Email válido
    }

    /**
     * Método adicional para sugerir correcciones
     */
    fun suggestCorrection(email: String): String? {
        val trimmedEmail = email.trim()
        if (!trimmedEmail.contains('@')) return null

        val domain = trimmedEmail.substringAfter('@')
        val commonMisspellings = mapOf(
            "gmial.com" to "gmail.com",
            "gmai.com" to "gmail.com",
            "gnail.com" to "gmail.com",
            "hotmial.com" to "hotmail.com",
            "yahooo.com" to "yahoo.com",
            "outlok.com" to "outlook.com"
        )

        return commonMisspellings[domain]
    }
}