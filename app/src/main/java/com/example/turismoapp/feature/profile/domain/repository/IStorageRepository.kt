package com.example.turismoapp.feature.profile.domain.repository

import android.net.Uri

interface IStorageRepository {
    suspend fun uploadFile(uri: Uri, path: String): Result<String>
}