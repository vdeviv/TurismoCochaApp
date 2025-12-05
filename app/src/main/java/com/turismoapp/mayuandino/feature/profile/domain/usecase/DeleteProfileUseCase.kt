package com.turismoapp.mayuandino.feature.profile.domain.usecase

import com.turismoapp.mayuandino.feature.profile.domain.repository.IProfileRepository

class DeleteProfileUseCase(
    private val repository: IProfileRepository
) {
    suspend fun invoke(uid: String): Result<Unit> {
        return repository.deleteProfile(uid)
    }
}