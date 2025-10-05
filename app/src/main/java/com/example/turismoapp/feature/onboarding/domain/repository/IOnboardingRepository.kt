package com.example.turismoapp.features.onboarding.domain.repository

import kotlinx.coroutines.flow.Flow

interface IOnboardingRepository {
    val isOnboardingCompleted: Flow<Boolean>
    suspend fun setOnboardingCompleted(completed: Boolean)
}
