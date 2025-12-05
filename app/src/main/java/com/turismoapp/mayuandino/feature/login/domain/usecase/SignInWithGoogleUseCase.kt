package com.turismoapp.mayuandino.feature.login.domain.usecase

import com.turismoapp.mayuandino.feature.login.data.repository.AuthRepository
import com.turismoapp.mayuandino.feature.login.domain.model.AuthUser
import com.turismoapp.mayuandino.feature.login.domain.model.Result

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