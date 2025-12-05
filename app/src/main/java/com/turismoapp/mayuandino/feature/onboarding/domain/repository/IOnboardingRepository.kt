package com.turismoapp.mayuandino.feature.onboarding.domain.repository

interface IOnboardingRepository {
    suspend fun isOnboardingCompleted(): Boolean
    suspend fun saveOnboardingCompleted(completed: Boolean)
}
