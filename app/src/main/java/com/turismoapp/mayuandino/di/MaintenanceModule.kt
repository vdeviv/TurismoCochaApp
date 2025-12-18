package com.turismoapp.mayuandino.di

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.turismoapp.mayuandino.feature.maintenance.data.MaintenanceDataStore
import com.turismoapp.mayuandino.feature.maintenance.data.MaintenanceRepository
import com.turismoapp.mayuandino.feature.maintenance.presentation.MaintenanceViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val maintenanceModule = module {

    // DataStore
    single {
        MaintenanceDataStore(androidContext())
    }

    // Firebase Remote Config
    single {
        FirebaseRemoteConfig.getInstance()
    }

    // Repository
    single {
        MaintenanceRepository(
            remoteConfig = get(),
            dataStore = get()
        )
    }

    // ViewModel
    viewModel {
        MaintenanceViewModel(
            repository = get()
        )
    }
}
