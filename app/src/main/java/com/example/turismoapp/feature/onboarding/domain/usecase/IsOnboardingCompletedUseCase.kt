package com.example.turismoapp.feature.onboarding.domain.usecase

import com.example.turismoapp.feature.onboarding.domain.repository.IOnboardingRepository

class IsOnboardingCompletedUseCase(
    private val repository: IOnboardingRepository
) {
    suspend operator fun invoke() = repository.isOnboardingCompleted()
}
