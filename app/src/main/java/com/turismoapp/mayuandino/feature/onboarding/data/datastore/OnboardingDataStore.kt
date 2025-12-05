package com.turismoapp.mayuandino.feature.onboarding.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
private val Context.dataStore by preferencesDataStore(name = "onboarding_prefs")

class OnboardingDataStore(private val context: Context) {

    private val IS_COMPLETED = booleanPreferencesKey("is_completed")

    val isCompleted: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[IS_COMPLETED] ?: false
    }
    suspend fun saveCompleted(completed: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[IS_COMPLETED] = completed
        }
    }
}
