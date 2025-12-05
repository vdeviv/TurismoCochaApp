package com.turismoapp.mayuandino.feature.onboarding.domain.usecase

import com.turismoapp.mayuandino.feature.onboarding.domain.repository.IOnboardingRepository

class IsOnboardingCompletedUseCase(
    private val repository: IOnboardingRepository
) {
    suspend operator fun invoke() = repository.isOnboardingCompleted()
}
