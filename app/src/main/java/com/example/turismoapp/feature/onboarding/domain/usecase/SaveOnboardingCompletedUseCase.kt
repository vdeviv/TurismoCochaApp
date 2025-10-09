package com.example.turismoapp.feature.onboarding.domain.usecase

import com.example.turismoapp.feature.onboarding.domain.repository.IOnboardingRepository

class SaveOnboardingCompletedUseCase(
    private val repository: IOnboardingRepository
) {
    suspend operator fun invoke(completed: Boolean) =
        repository.saveOnboardingCompleted(completed)
}
