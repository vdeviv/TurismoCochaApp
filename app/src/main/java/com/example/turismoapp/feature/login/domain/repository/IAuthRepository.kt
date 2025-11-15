package com.example.turismoapp.feature.login.domain.repository

import com.example.turismoapp.feature.login.domain.model.AuthUser
import com.example.turismoapp.feature.login.domain.model.Result
import kotlinx.coroutines.flow.Flow

interface IAuthRepository {

    suspend fun signUp(email: String, password: String): Result<AuthUser>

    suspend fun signIn(email: String, password: String): Result<AuthUser>

    val currentUser: Flow<AuthUser?>
}