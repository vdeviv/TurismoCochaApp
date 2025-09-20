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
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar Koin
        startKoin {
            androidContext(this@MainActivity)
            modules(appModule) // Asegúrate de que 'appModule' esté correctamente definido
        }

        // Enable edge-to-edge display if needed
        enableEdgeToEdge()

        // Set the content view
        setContent {
            TurismoAppTheme {
                AppNavigation() // Aquí se cargará tu pantalla de navegación
            }
        }
    }

    // Define tu módulo de Koin aquí
    private val appModule: Module = module {
        // Registrar las fuentes de datos
        single { DollarLocalDataSource(get()) }
        single { RealTimeRemoteDataSource() }

        // Registrar el repositorio, inyectando las dependencias necesarias
        single<IDollarRepository> { DollarRepository(get(), get()) }

        // Registrar FetchDollarUseCase
        single { FetchDollarUseCase(get()) }

        // Registrar el ViewModel
        viewModel { DollarViewModel(fetchDollarUseCase = get()) }
    }
}
