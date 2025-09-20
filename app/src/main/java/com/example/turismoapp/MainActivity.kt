package com.example.turismoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.turismoapp.feature.dollar.data.repository.DollarRepository
import com.example.turismoapp.feature.dollar.datasource.DollarLocalDataSource
import com.example.turismoapp.feature.dollar.datasource.RealTimeRemoteDataSource
import com.example.turismoapp.feature.dollar.domain.repository.IDollarRepository
import com.example.turismoapp.feature.dollar.domain.usecase.FetchDollarUseCase
import com.example.turismoapp.feature.dollar.presentation.DollarViewModel
import com.example.turismoapp.feature.navigation.AppNavigation
import com.example.turismoapp.ui.theme.TurismoAppTheme
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.androidx.viewmodel.dsl.viewModel



import androidx.room.Room
import com.example.turismoapp.feature.dollar.data.database.AppRoomDatabase
import com.example.turismoapp.feature.dollar.data.database.dao.IDollarDao
import com.example.turismoapp.feature.navigation.AppNavigation
import org.koin.android.ext.koin.androidLogger
import org.koin.core.logger.Level

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Configuración de Koin
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@MainActivity)
            modules(appModule)
        }

        setContent {
            TurismoAppTheme {
                AppNavigation()
            }
        }
    }
}

val appModule = module {

    // Definición de la base de datos de Room como un singleton
    single {
        Room.databaseBuilder(
            androidContext(),
            AppRoomDatabase::class.java,
            "dollar_db"
        ).build()
    }

    // Definición del DAO de Room como un singleton
    single {
        get<AppRoomDatabase>().dollarDao() as IDollarDao
    }

    // Registrar el repositorio
    single<IDollarRepository> { DollarRepository(get(), get()) }

    // Registrar FetchDollarUseCase
    single { FetchDollarUseCase(get()) }

    viewModel { DollarViewModel(get()) }
}

