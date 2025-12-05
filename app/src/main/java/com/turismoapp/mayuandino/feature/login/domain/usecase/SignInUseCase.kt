package com.turismoapp.mayuandino.feature.login.domain.usecase

import com.turismoapp.mayuandino.feature.login.domain.model.AuthUser
import com.turismoapp.mayuandino.feature.login.domain.model.Result // <<-- IMPORTANTE: Importamos tu Result
import com.turismoapp.mayuandino.feature.login.domain.repository.IAuthRepository

class SignInUseCase(
    private val repository: IAuthRepository
) {
    // El tipo de retorno ahora es correcto: com.example.turismoapp.feature.login.domain.model.Result<AuthUser>
    suspend operator fun invoke(email: String, password: String): Result<AuthUser> =
        repository.signIn(email.trim(), password)
}