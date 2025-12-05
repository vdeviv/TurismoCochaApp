package com.turismoapp.mayuandino.feature.profile.domain.usecase

import com.turismoapp.mayuandino.feature.profile.domain.model.ProfileModel
import com.turismoapp.mayuandino.feature.profile.domain.repository.IProfileRepository

class UpdateProfileUseCase(
    private val repository: IProfileRepository
) {
    suspend fun invoke(profile: ProfileModel): Result<Unit> {
        return repository.updateProfile(profile)
    }
}