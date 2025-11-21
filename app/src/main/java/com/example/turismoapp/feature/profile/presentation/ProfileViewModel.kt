package com.example.turismoapp.feature.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.turismoapp.feature.profile.domain.model.ProfileModel
import com.example.turismoapp.feature.profile.domain.usecase.DeleteProfileUseCase
import com.example.turismoapp.feature.profile.domain.usecase.GetProfileUseCase
import com.example.turismoapp.feature.profile.domain.usecase.SaveProfileUseCase
import com.example.turismoapp.feature.profile.domain.usecase.UpdateProfileUseCase
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val getProfileUseCase: GetProfileUseCase,
    private val saveProfileUseCase: SaveProfileUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase,
    private val deleteProfileUseCase: DeleteProfileUseCase,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    sealed class ProfileUiState {
        object Init : ProfileUiState()
        object Loading : ProfileUiState()
        data class Error(val message: String) : ProfileUiState()
        data class Success(val profile: ProfileModel) : ProfileUiState()
        object Deleted : ProfileUiState()
    }

    private var _state = MutableStateFlow<ProfileUiState>(ProfileUiState.Init)
    val state: StateFlow<ProfileUiState> = _state.asStateFlow()

    // Cargar perfil del usuario actual
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
                onSuccess = {
                    _state.value = ProfileUiState.Success(it)
                },
                onFailure = {
                    // Si el perfil no existe, crear uno nuevo con datos básicos
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

    // Guardar perfil nuevo
    fun saveProfile(profile: ProfileModel) {
        viewModelScope.launch(Dispatchers.IO) {
            _state.value = ProfileUiState.Loading
            val result = saveProfileUseCase.invoke(profile)
            result.fold(
                onSuccess = {
                    _state.value = ProfileUiState.Success(profile)
                },
                onFailure = {
                    _state.value = ProfileUiState.Error(it.message ?: "Error al guardar")
                }
            )
        }
    }

    // Actualizar perfil existente
    fun updateProfile(name: String, email: String, phone: String, summary: String) {
        val currentState = _state.value
        if (currentState !is ProfileUiState.Success) return

        val updatedProfile = currentState.profile.copy(
            name = name,
            email = email,
            cellphone = phone,
            summary = summary
        )

        viewModelScope.launch(Dispatchers.IO) {
            _state.value = ProfileUiState.Loading
            val result = updateProfileUseCase.invoke(updatedProfile)
            result.fold(
                onSuccess = {
                    _state.value = ProfileUiState.Success(updatedProfile)
                },
                onFailure = {
                    _state.value = ProfileUiState.Error(it.message ?: "Error al actualizar")
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

            // Primero eliminar el perfil de Firestore
            val deleteProfileResult = deleteProfileUseCase.invoke(uid)

            deleteProfileResult.fold(
                onSuccess = {
                    // Luego eliminar la cuenta de Firebase Auth
                    try {
                        firebaseAuth.currentUser?.delete()?.await()
                        _state.value = ProfileUiState.Deleted
                    } catch (e: Exception) {
                        _state.value = ProfileUiState.Error("Error al eliminar cuenta: ${e.localizedMessage}")
                    }
                },
                onFailure = {
                    _state.value = ProfileUiState.Error(it.message ?: "Error al eliminar perfil")
                }
            )
        }
    }

    // Cerrar sesión
    fun signOut() {
        firebaseAuth.signOut()
        _state.value = ProfileUiState.Init
    }
}