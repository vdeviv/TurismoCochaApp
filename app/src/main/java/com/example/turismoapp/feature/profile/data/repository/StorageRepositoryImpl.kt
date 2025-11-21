package com.example.turismoapp.feature.profile.data.repository

import android.net.Uri
import com.example.turismoapp.feature.profile.domain.repository.IStorageRepository
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

class StorageRepositoryImpl(private val storage: FirebaseStorage
) : IStorageRepository {

    override suspend fun uploadFile(uri: Uri, path: String): Result<String> = try {
        val storageRef = storage.reference.child(path)
        val uploadTask = storageRef.putFile(uri).await()
        // Obtener la URL de descarga para guardarla en Firestore
        val downloadUrl = uploadTask.storage.downloadUrl.await().toString()
        Result.success(downloadUrl)
    } catch (e: Exception) {
        Result.failure(e)
    }
}