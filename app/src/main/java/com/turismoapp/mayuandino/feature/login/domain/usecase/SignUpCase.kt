package com.turismoapp.mayuandino.feature.login.domain.usecase

import com.turismoapp.mayuandino.feature.login.domain.model.AuthUser
import com.turismoapp.mayuandino.feature.login.domain.model.Result // <<-- IMPORTANTE: Importamos tu Result
import com.turismoapp.mayuandino.feature.login.domain.repository.IAuthRepository

class SignUpUseCase(
    private val repository: IAuthRepository,
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase
) {

    suspend operator fun invoke(email: String, password: String): Result<AuthUser> {
        // 1. Validar el email
        val emailValidationResult = validateEmailUseCase(email)
        if (emailValidationResult != null) {
            return Result.Error(Exception(emailValidationResult))
        }

        // 2. Validar la contraseña (puedes añadir más reglas aquí)
        val passwordValidationResult = validatePasswordUseCase(password)
        if (passwordValidationResult != null) {
            return Result.Error(Exception(passwordValidationResult))
        }

        // 3. Llamar al repositorio para crear el usuario en Firebase
        return repository.signUp(email, password)
    }
}