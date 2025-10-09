package com.example.turismoapp.feature.onboarding.data.repository

import com.example.turismoapp.feature.onboarding.data.datastore.OnboardingDataStore
import com.example.turismoapp.feature.onboarding.domain.repository.IOnboardingRepository
import kotlinx.coroutines.flow.first

class OnboardingRepository(
    private val dataStore: OnboardingDataStore
) : IOnboardingRepository {

    override suspend fun isOnboardingCompleted(): Boolean {
        return dataStore.isCompleted.first()
    }

    override suspend fun saveOnboardingCompleted(completed: Boolean) {
        dataStore.saveCompleted(completed)
    }
}
