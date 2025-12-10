package com.turismoapp.mayuandino.feature.profile.presentation

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.turismoapp.mayuandino.feature.profile.domain.model.ProfileModel
import com.turismoapp.mayuandino.feature.profile.domain.usecase.*
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ProfileViewModel(
    private val getProfileUseCase: GetProfileUseCase,
    private val saveProfileUseCase: SaveProfileUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase,
    private val deleteProfileUseCase: DeleteProfileUseCase,
    private val uploadAvatarUseCase: UploadAvatarUseCase,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    sealed class ProfileUiState {
        object Init : ProfileUiState()
        object Loading : ProfileUiState()
        data class Error(val message: String) : ProfileUiState()
        data class Success(val profile: ProfileModel) : ProfileUiState()
        object Deleted : ProfileUiState()
    }

    private val _state = MutableStateFlow<ProfileUiState>(ProfileUiState.Init)
    val state: StateFlow<ProfileUiState> = _state.asStateFlow()

    // Cargar perfil del usuario
    fun loadProfile() {
        val uid = firebaseAuth.currentUser?.uid
        val authEmail = firebaseAuth.currentUser?.email ?: "" // Email de Auth

        if (uid == null) {
            _state.value = ProfileUiState.Error("Usuario no autenticado (UID nulo)")
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            _state.value = ProfileUiState.Loading

            val result = getProfileUseCase.invoke(uid)
            result.fold(
                onSuccess = { profile ->
                    // ÉXITO: El perfil existe en Firestore
                    Log.d("PROFILE_LOAD", "Perfil cargado: ${profile.name} (${profile.email})")
                    _state.value = ProfileUiState.Success(profile)
                },
                onFailure = {
                    // FALLO: El perfil NO existe en Firestore (lo creamos)
                    if (authEmail.isNotEmpty()) {
                        Log.w("PROFILE_LOAD", "Perfil no encontrado. Creando inicial con email de Auth.")
                        val newProfile = ProfileModel(
                            uid = uid,
                            email = authEmail, // Usamos el email de Auth
                            name = "",
                            cellphone = "",
                            summary = "",
                            pathUrl = "https://cdn-icons-png.flaticon.com/512/149/149071.png"
                        )
                        saveProfile(newProfile)
                    } else {
                        // Si no hay email de Auth, hay un problema de autenticación o el usuario es anónimo.
                        val msg = "Error: Usuario logueado sin correo electrónico."
                        Log.e("PROFILE_LOAD", msg)
                        _state.value = ProfileUiState.Error(msg)
                    }
                }
            )
        }
    }

    // Guardar perfil (usado para crear el perfil inicial)
    fun saveProfile(profile: ProfileModel) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = saveProfileUseCase.invoke(profile)
            result.fold(
                onSuccess = {
                    Log.d("SAVE_SUCCESS", "Perfil creado/guardado exitosamente.")
                    _state.value = ProfileUiState.Success(profile)
                },
                onFailure = {
                    Log.e("SAVE_ERROR", "Error crítico al CREAR/GUARDAR perfil en Firestore: ${it.message}", it)
                    _state.value = ProfileUiState.Error("Error al guardar perfil: ${it.message}")
                }
            )
        }
    }

    // Actualizar perfil (mantiene el email original)
    fun updateProfile(name: String, email: String, phone: String, summary: String) {
        val current = _state.value

        if (current !is ProfileUiState.Success) {
            Log.e("UPDATE_FAIL", "Intento de actualización fallido. Estado actual: ${current::class.simpleName}")
            return // Salida silenciosa si el estado no está listo
        }

        // Mantenemos el email y la URL del avatar actuales que están en el estado
        val updated = current.profile.copy(
            name = name,
            email = current.profile.email,
            cellphone = phone,
            summary = summary
        )

        Log.d("UPDATE_CHECK", "Enviando a Firestore | UID: ${updated.uid} | Nombre: ${updated.name}")

        viewModelScope.launch(Dispatchers.IO) {
            updateProfileUseCase.invoke(updated).fold(
                onSuccess = {
                    Log.d("UPDATE_SUCCESS", "Perfil actualizado correctamente en Firestore.")
                    _state.value = ProfileUiState.Success(updated)
                },
                onFailure = {
                    Log.e("UPDATE_ERROR", "Error al ACTUALIZAR perfil (Firestore): ${it.message}", it)
                    _state.value = ProfileUiState.Error("Error al actualizar perfil: ${it.message}")
                }
            )
        }
    }

    // Subir nueva imagen de avatar
    fun uploadNewAvatar(uri: Uri) {
        val uid = firebaseAuth.currentUser?.uid ?: return

        viewModelScope.launch(Dispatchers.IO) {
            // Temporalmente, podemos mostrar un loading específico o usar el general si es rápido
            // _state.value = ProfileUiState.Loading // (Opcional si la subida es muy lenta)

            val uploadResult = uploadAvatarUseCase.invoke(uid, uri)
            uploadResult.fold(
                onSuccess = { newUrl ->
                    val current = _state.value
                    if (current is ProfileUiState.Success) {
                        // Actualizar solo el pathUrl y luego guardar el modelo actualizado en Firestore
                        val updated = current.profile.copy(pathUrl = newUrl)
                        updateProfileUseCase.invoke(updated).fold(
                            onSuccess = { _state.value = ProfileUiState.Success(updated) },
                            onFailure = { _state.value = ProfileUiState.Error("Error al actualizar URL de imagen") }
                        )
                    }
                },
                onFailure = {
                    _state.value = ProfileUiState.Error("Error al subir imagen: ${it.message}")
                }
            )
        }
    }


    // Eliminar cuenta completa
    fun deleteAccount() {
        val uid = firebaseAuth.currentUser?.uid
        if (uid == null) {
            _state.value = ProfileUiState.Error("Usuario no autenticado")
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            _state.value = ProfileUiState.Loading
            deleteProfileUseCase.invoke(uid).fold( // Elimina el perfil de Firestore
                onSuccess = {
                    try {
                        firebaseAuth.currentUser?.delete()?.await() // Elimina el usuario de Auth
                        _state.value = ProfileUiState.Deleted // Señal para la navegación
                    } catch (e: Exception) {
                        _state.value = ProfileUiState.Error("Error al eliminar cuenta: ${e.localizedMessage}")
                    }
                },
                onFailure = { _state.value = ProfileUiState.Error(it.message ?: "Error al eliminar perfil") }
            )
        }
    }
    fun signOut() {
        firebaseAuth.signOut()
        _state.value = ProfileUiState.Init // Reinicia el estado para forzar la redirección
    }
}