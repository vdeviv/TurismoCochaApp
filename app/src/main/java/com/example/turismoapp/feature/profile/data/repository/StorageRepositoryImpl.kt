package com.example.turismoapp.feature.profile.data.repository

import android.net.Uri
import com.example.turismoapp.feature.profile.domain.repository.IStorageRepository
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

class StorageRepositoryImpl(
    private val storage: FirebaseStorage // Inyectamos la instancia de Firebase Storage
) : IStorageRepository {

    override suspend fun uploadFile(fileUri: Uri, path: String): Result<String> {
        return try {
            val storageRef = storage.reference.child(path)

            // 1. Subir el archivo
            storageRef.putFile(fileUri).await()

            // 2. Obtener la URL de descarga (el pathUrl)
            val downloadUrl = storageRef.downloadUrl.await().toString()

            Result.success(downloadUrl)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}