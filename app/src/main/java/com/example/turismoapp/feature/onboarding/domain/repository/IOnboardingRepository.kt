package com.example.turismoapp.feature.onboarding.domain.repository

interface IOnboardingRepository {
    suspend fun isOnboardingCompleted(): Boolean
    suspend fun saveOnboardingCompleted(completed: Boolean)
}
