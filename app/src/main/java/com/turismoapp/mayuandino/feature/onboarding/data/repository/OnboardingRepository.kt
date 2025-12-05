package com.turismoapp.mayuandino.feature.onboarding.data.repository

import com.turismoapp.mayuandino.feature.onboarding.data.datastore.OnboardingDataStore
import com.turismoapp.mayuandino.feature.onboarding.domain.repository.IOnboardingRepository
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
