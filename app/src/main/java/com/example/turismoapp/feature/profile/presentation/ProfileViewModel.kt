package com.example.turismoapp.feature.profile.presentation

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.turismoapp.feature.profile.domain.model.ProfileModel
import com.example.turismoapp.feature.profile.domain.usecase.*
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
        if (uid == null) {
            _state.value = ProfileUiState.Error("Usuario no autenticado")
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            _state.value = ProfileUiState.Loading

            val result = getProfileUseCase.invoke(uid)
            result.fold(
                onSuccess = { profile ->
                    _state.value = ProfileUiState.Success(profile)
                },
                onFailure = {
                    // Si no existe perfil, creamos uno inicial
                    val currentUser = firebaseAuth.currentUser
                    if (currentUser != null) {
                        val newProfile = ProfileModel(
                            uid = currentUser.uid,
                            email = currentUser.email ?: "",
                            name = "",
                            cellphone = "",
                            summary = "",
                            pathUrl = "https://cdn-icons-png.flaticon.com/512/149/149071.png"
                        )
                        saveProfile(newProfile)
                    } else {
                        _state.value = ProfileUiState.Error(it.message ?: "Error desconocido")
                    }
                }
            )
        }
    }

    // Guardar perfil
    fun saveProfile(profile: ProfileModel) {
        viewModelScope.launch(Dispatchers.IO) {
            _state.value = ProfileUiState.Loading
            val result = saveProfileUseCase.invoke(profile)
            result.fold(
                onSuccess = { _state.value = ProfileUiState.Success(profile) },
                onFailure = {
                    Log.e("SaveProfileError", "Error al guardar perfil", it)
                    _state.value = ProfileUiState.Error(it.message ?: "Error desconocido")
                }
            )
        }
    }

    // Actualizar perfil
    fun updateProfile(name: String, email: String, phone: String, summary: String) {
        val current = _state.value as? ProfileUiState.Success ?: return
        val updated = current.profile.copy(
            name = name,
            email = current.profile.email,
            cellphone = phone,
            summary = summary
        )
        viewModelScope.launch(Dispatchers.IO) {
            _state.value = ProfileUiState.Loading
            updateProfileUseCase.invoke(updated).fold(
                onSuccess = { _state.value = ProfileUiState.Success(updated) },
                onFailure = { _state.value = ProfileUiState.Error(it.message ?: "Error al actualizar") }
            )
        }
    }

    // Eliminar perfil
    fun deleteAccount() {
        val uid = firebaseAuth.currentUser?.uid
        if (uid == null) {
            _state.value = ProfileUiState.Error("Usuario no autenticado")
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            _state.value = ProfileUiState.Loading
            deleteProfileUseCase.invoke(uid).fold(
                onSuccess = {
                    try {
                        firebaseAuth.currentUser?.delete()?.await()
                        _state.value = ProfileUiState.Deleted
                    } catch (e: Exception) {
                        _state.value = ProfileUiState.Error("Error al eliminar cuenta: ${e.localizedMessage}")
                    }
                },
                onFailure = { _state.value = ProfileUiState.Error(it.message ?: "Error al eliminar perfil") }
            )
        }
    }

    // Subir avatar
    fun uploadNewAvatar(uri: Uri) {
        val uid = firebaseAuth.currentUser?.uid ?: return
        viewModelScope.launch(Dispatchers.IO) {
            val uploadResult = uploadAvatarUseCase.invoke(uid, uri)
            uploadResult.fold(
                onSuccess = { newUrl ->
                    val current = _state.value
                    if (current is ProfileUiState.Success) {
                        val updated = current.profile.copy(pathUrl = newUrl)
                        updateProfileUseCase.invoke(updated).fold(
                            onSuccess = { _state.value = ProfileUiState.Success(updated) },
                            onFailure = { _state.value = ProfileUiState.Error("Error al actualizar imagen") }
                        )
                    }
                },
                onFailure = { _state.value = ProfileUiState.Error("Error al subir imagen: ${it.message}") }
            )
        }
    }
}
