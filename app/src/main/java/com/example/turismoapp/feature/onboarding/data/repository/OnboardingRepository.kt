package com.example.turismoapp.features.onboarding.data.repository

import com.example.turismoapp.features.onboarding.data.datastore.OnboardingDataStore
import com.example.turismoapp.features.onboarding.domain.repository.IOnboardingRepository
import kotlinx.coroutines.flow.Flow

class OnboardingRepository(
    private val dataStore: OnboardingDataStore
) : IOnboardingRepository {

    override val isOnboardingCompleted: Flow<Boolean>
        get() = dataStore.isOnboardingCompleted

    override suspend fun setOnboardingCompleted(completed: Boolean) {
        dataStore.setOnboardingCompleted(completed)
    }
}
