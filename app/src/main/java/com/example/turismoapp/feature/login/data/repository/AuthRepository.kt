// feature/login/data/repository/AuthRepository.kt

package com.example.turismoapp.feature.login.data.repository

import com.example.turismoapp.feature.login.domain.model.AuthUser
import com.example.turismoapp.feature.login.domain.repository.IAuthRepository
import kotlinx.coroutines.delay

// ⭐️ SOLUCIÓN: Usamos un objeto singleton en memoria para simular una 'Base de Datos'.
// Esto asegura que la lista de usuarios sea la misma en todas las instancias de AuthRepository.
private object InMemoryStore {
    // Almacena a los usuarios iniciales (AuthUser) y sus contraseñas (String).
    val users: MutableList<Pair<AuthUser, String>> = mutableListOf(
        AuthUser(id = "1", email = "demo@cocha.com", displayName = "Demo User") to "123456",
        AuthUser(id = "2", email = "vivi@cocha.com", displayName = "Vivi") to "123456"
    )
}

class AuthRepository : IAuthRepository {
    override suspend fun signIn(email: String, password: String): Result<AuthUser> {
        delay(700)

        // 1. Buscar al usuario en la lista compartida
        val userEntry = InMemoryStore.users.find {
            it.first.email.equals(email, true) && it.second == password
        }

        return if (userEntry != null) {
            // Éxito: se encontró el par email/contraseña
            Result.success(userEntry.first)
        } else {
            // Falla: credenciales no encontradas o incorrectas
            Result.failure(IllegalArgumentException("Credenciales inválidas"))
        }
    }

    override suspend fun signUp(email: String, password: String): Result<AuthUser> {
        delay(1000)

        // 1. Comprobar si el correo ya existe
        val existingEmail = InMemoryStore.users.any { it.first.email.equals(email, true) }
        if (existingEmail) {
            return Result.failure(IllegalStateException("El correo ya está en uso"))
        }

        // 2. Crear y construir el nuevo AuthUser
        val newUserId = (InMemoryStore.users.size + 1).toString()
        val defaultDisplayName = email.substringBefore('@')
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }

        val newUser = AuthUser(
            id = newUserId,
            email = email.trim(),
            displayName = defaultDisplayName
        )

        // 3. ⭐️ GUARDAR AL NUEVO USUARIO Y SU CONTRASEÑA EN LA MEMORIA COMPARTIDA ⭐️
        InMemoryStore.users.add(newUser to password)

        return Result.success(newUser)
    }
}