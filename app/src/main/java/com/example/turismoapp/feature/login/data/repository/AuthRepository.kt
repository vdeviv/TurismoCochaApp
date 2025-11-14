package com.example.turismoapp.feature.login.data.repository

import com.example.turismoapp.feature.login.domain.model.AuthUser
import com.example.turismoapp.feature.login.domain.model.Result
import com.example.turismoapp.feature.login.domain.repository.IAuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class AuthRepository(
    private val firebaseAuth: FirebaseAuth
) : IAuthRepository {

    override val currentUser: Flow<AuthUser?> = flow {
        // En una app real usarías onAuthStateChangedListener
        emit(firebaseAuth.currentUser?.let {
            AuthUser(it.uid, it.email)
        })
    }

    override suspend fun signUp(email: String, password: String): Result<AuthUser> {
        return try {
            val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = authResult.user

            if (firebaseUser != null) {
                val authUser = AuthUser(firebaseUser.uid, firebaseUser.email)
                Result.Success(authUser) // Usando tu Result.Success
            } else {
                Result.Error(Exception("Registro fallido. El usuario es nulo.")) // Usando tu Result.Error
            }
        } catch (e: Exception) {
            val errorMessage = when (e) {
                is FirebaseAuthWeakPasswordException -> "La contraseña es demasiado débil."
                is FirebaseAuthInvalidCredentialsException -> "Email o contraseña inválidos."
                is FirebaseAuthUserCollisionException -> "Ya existe una cuenta con este email."
                else -> e.localizedMessage ?: "Error desconocido al registrar."
            }
            Result.Error(Exception(errorMessage)) // Usando tu Result.Error
        }
    }

    override suspend fun signIn(email: String, password: String): Result<AuthUser> {
        return try {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val firebaseUser = firebaseAuth.currentUser
            if (firebaseUser != null) {
                Result.Success(AuthUser(firebaseUser.uid, firebaseUser.email))
            } else {
                Result.Error(Exception("Error al iniciar sesión."))
            }
        } catch (e: Exception) {
            Result.Error(Exception(e.localizedMessage ?: "Error al iniciar sesión."))
        }
    }
}