package com.example.turismoapp.features.onboarding.domain.usecase

import com.example.turismoapp.features.onboarding.domain.repository.IOnboardingRepository

class SaveOnboardingCompletedUseCase(
    private val repository: IOnboardingRepository
) {
    suspend operator fun invoke(completed: Boolean) {
        repository.setOnboardingCompleted(completed)
    }
}
