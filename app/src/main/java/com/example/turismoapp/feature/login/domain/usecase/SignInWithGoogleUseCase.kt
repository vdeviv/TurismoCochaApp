package com.example.turismoapp.feature.login.domain.usecase

import com.example.turismoapp.feature.login.data.repository.AuthRepository
import com.example.turismoapp.feature.login.domain.model.AuthUser
import com.example.turismoapp.feature.login.domain.model.Result

class SignInWithGoogleUseCase(
    private val authRepository: AuthRepository
) {
    /**
     * Usa el ID Token de Google para autenticar en Firebase.
     */
    suspend operator fun invoke(idToken: String): Result<AuthUser> {
        return authRepository.signInWithGoogleCredential(idToken)
    }
}