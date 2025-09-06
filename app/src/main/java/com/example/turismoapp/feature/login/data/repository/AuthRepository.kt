package com.example.turismoapp.feature.login.data.repository

import com.example.turismoapp.feature.login.domain.model.AuthUser
import com.example.turismoapp.feature.login.domain.repository.IAuthRepository
import kotlinx.coroutines.delay

class AuthRepository : IAuthRepository {
    override suspend fun signIn(email: String, password: String): Result<AuthUser> {
        delay(700)
        val ok = (email.equals("demo@cocha.com", true) || email.equals("vivi@cocha.com", true)) &&
                password == "123456"
        return if (ok) {
            Result.success(AuthUser(id = "1", email = email, displayName = "Vivi"))
        } else {
            Result.failure(IllegalArgumentException("Credenciales inválidas"))
        }
    }
}
