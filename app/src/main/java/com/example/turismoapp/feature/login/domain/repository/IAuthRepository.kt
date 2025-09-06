package com.example.turismoapp.feature.login.domain.repository

import com.example.turismoapp.feature.login.domain.model.AuthUser

interface IAuthRepository {
    suspend fun signIn(email: String, password: String): Result<AuthUser>
}
