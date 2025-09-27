package com.example.turismoapp.feature.profile.domain.usecase

import com.example.turismoapp.feature.profile.domain.model.ProfileModel
import com.example.turismoapp.feature.profile.domain.repository.IProfileRepository
import kotlinx.coroutines.delay

class GetProfileUseCase(
    val repository: IProfileRepository
) {
    suspend fun invoke(): Result<ProfileModel> {
        delay(3000)
        return repository.fetchData()
    }
}