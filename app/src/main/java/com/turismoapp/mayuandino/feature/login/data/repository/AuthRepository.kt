package com.turismoapp.mayuandino.feature.login.data.repository

import com.turismoapp.mayuandino.feature.login.domain.model.AuthUser
import com.turismoapp.mayuandino.feature.login.domain.model.Result
import com.turismoapp.mayuandino.feature.login.domain.repository.IAuthRepository
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class AuthRepository(
    private val firebaseAuth: FirebaseAuth
) : IAuthRepository {

    override val currentUser: Flow<AuthUser?> = flow {
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
                Result.Success(authUser)
            } else {
                Result.Error(Exception("Registro fallido. El usuario es nulo."))
            }
        } catch (e: Exception) {
            val errorMessage = when (e) {
                is FirebaseAuthWeakPasswordException -> "La contraseña es demasiado débil."
                is FirebaseAuthInvalidCredentialsException -> "Email o contraseña inválidos."
                is FirebaseAuthUserCollisionException -> "Ya existe una cuenta con este email."
                else -> e.localizedMessage ?: "Error desconocido al registrar."
            }
            Result.Error(Exception(errorMessage))
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
            val errorMessage = when (e) {
                is FirebaseAuthInvalidCredentialsException -> "Email o contraseña incorrectos."
                else -> e.localizedMessage ?: "Error al iniciar sesión."
            }
            Result.Error(Exception(errorMessage))
        }
    }
    fun getGoogleCredential(idToken: String): AuthCredential {
        return GoogleAuthProvider.getCredential(idToken, null)
    }

    /**
     * Inicia sesión en Firebase usando una credencial de Google.
     */
    suspend fun signInWithGoogleCredential(idToken: String): Result<AuthUser> {
        return try {
            val credential = getGoogleCredential(idToken)
            val authResult = firebaseAuth.signInWithCredential(credential).await()
            val user = authResult.user

            if (user != null) {
                // El correo está automáticamente verificado por ser Google.
                Result.Success(AuthUser(user.uid, user.email))
            } else {
                Result.Error(Exception("Error al autenticar con Google: usuario nulo."))
            }
        } catch (e: Exception) {
            val errorMessage = when (e) {
                // Puedes añadir manejo específico para errores de credenciales, etc.
                else -> e.localizedMessage ?: "Error desconocido al iniciar sesión con Google."
            }
            Result.Error(Exception(errorMessage))
        }
    }

}