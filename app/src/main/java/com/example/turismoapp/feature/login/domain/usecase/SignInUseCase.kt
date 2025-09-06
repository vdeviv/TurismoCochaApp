package com.example.turismoapp.feature.login.domain.usecase

import com.example.turismoapp.feature.login.domain.model.AuthUser
import com.example.turismoapp.feature.login.domain.repository.IAuthRepository

class SignInUseCase(
    private val repository: IAuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<AuthUser> =
        repository.signIn(email.trim(), password)
}