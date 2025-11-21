package com.example.turismoapp.feature.profile.domain.usecase

import com.example.turismoapp.feature.profile.domain.model.ProfileModel
import com.example.turismoapp.feature.profile.domain.repository.IProfileRepository

class SaveProfileUseCase(
    private val repository: IProfileRepository
) {
    suspend fun invoke(profile: ProfileModel): Result<Unit> {
        return repository.saveProfile(profile)
    }
}