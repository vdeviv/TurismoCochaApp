package com.example.turismoapp.feature.profile.domain.usecase

import com.example.turismoapp.feature.profile.domain.repository.IProfileRepository

class DeleteProfileUseCase(
    private val repository: IProfileRepository
) {
    suspend fun invoke(uid: String): Result<Unit> {
        return repository.deleteProfile(uid)
    }
}