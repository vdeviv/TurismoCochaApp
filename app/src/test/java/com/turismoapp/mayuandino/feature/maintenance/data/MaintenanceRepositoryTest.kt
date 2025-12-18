package com.turismoapp.mayuandino.feature.maintenance.data

import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test

class MaintenanceRepositoryTest {
    private val dataStore = mockk<MaintenanceDataStore>(relaxed = true)
    private val remoteConfig = mockk<com.google.firebase.remoteconfig.FirebaseRemoteConfig>(relaxed = true)

    private val repository = MaintenanceRepository(remoteConfig, dataStore)

    @Test
    fun `cuando el modo mantenimiento se activa en la nube se guarda en el local`() = runBlocking {
        repository.fetchMaintenanceStatus().collect { /* Act */ }

        coVerify { dataStore.setMaintenanceMode(any()) }
    }
}