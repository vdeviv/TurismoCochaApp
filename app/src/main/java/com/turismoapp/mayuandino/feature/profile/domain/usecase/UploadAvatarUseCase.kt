package com.turismoapp.mayuandino.feature.profile.domain.usecase
import android.net.Uri

import com.turismoapp.mayuandino.feature.profile.domain.repository.IStorageRepository

class UploadAvatarUseCase(
    private val repository: IStorageRepository // Inyectamos el repositorio de Storage
) {
    suspend fun invoke(userId: String, imageUri: Uri): Result<String> {
        val path = "users/$userId/avatar.jpg" // Ruta canónica para el avatar
        return repository.uploadFile(imageUri, path)
    }
}