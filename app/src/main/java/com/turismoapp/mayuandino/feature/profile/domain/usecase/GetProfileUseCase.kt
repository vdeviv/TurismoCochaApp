package com.turismoapp.mayuandino.feature.profile.domain.usecase

import com.turismoapp.mayuandino.feature.profile.domain.model.ProfileModel
import com.turismoapp.mayuandino.feature.profile.domain.repository.IProfileRepository

class GetProfileUseCase(
    private val repository: IProfileRepository
) {
    suspend fun invoke(uid: String): Result<ProfileModel> {
        return repository.fetchProfile(uid)
    }
}