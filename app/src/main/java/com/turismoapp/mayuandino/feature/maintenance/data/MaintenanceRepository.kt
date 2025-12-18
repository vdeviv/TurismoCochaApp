package com.turismoapp.mayuandino.feature.maintenance.data

import android.util.Log
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.turismoapp.mayuandino.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await


class MaintenanceRepository(
    private val remoteConfig: FirebaseRemoteConfig,
    private val dataStore: MaintenanceDataStore
) {

    init {
        setupRemoteConfig()
    }

    private fun setupRemoteConfig() {
        val settings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(5)
            .build()

        remoteConfig.setConfigSettingsAsync(settings)
        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
    }

    fun fetchMaintenanceStatus(): Flow<Boolean> = flow {
        try {
            remoteConfig.fetchAndActivate().await()
            val enabled = remoteConfig.getBoolean("maintenance_mode")

            dataStore.setMaintenanceMode(enabled)
            emit(enabled)

            Log.d("Maintenance", "maintenance_mode=$enabled")

        } catch (e: Exception) {
            emit(false)
            Log.e("Maintenance", "Error RemoteConfig", e)
        }
    }

    fun observeMaintenanceStatus(): Flow<Boolean> =
        dataStore.observeMaintenanceMode()

}
