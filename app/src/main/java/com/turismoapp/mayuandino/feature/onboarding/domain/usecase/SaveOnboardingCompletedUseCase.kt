package com.turismoapp.mayuandino.feature.onboarding.domain.usecase

import com.turismoapp.mayuandino.feature.onboarding.domain.repository.IOnboardingRepository

class SaveOnboardingCompletedUseCase(
    private val repository: IOnboardingRepository
) {
    suspend operator fun invoke(completed: Boolean) =
        repository.saveOnboardingCompleted(completed)
}
