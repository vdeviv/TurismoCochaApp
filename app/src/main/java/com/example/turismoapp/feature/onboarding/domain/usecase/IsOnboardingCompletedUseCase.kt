package com.example.turismoapp.features.onboarding.domain.usecase

import com.example.turismoapp.features.onboarding.domain.repository.IOnboardingRepository

class IsOnboardingCompletedUseCase(
    private val repository: IOnboardingRepository
) {
    operator fun invoke() = repository.isOnboardingCompleted
}
