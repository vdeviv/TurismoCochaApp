package com.example.turismoapp.feature.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room // Necesario para construir la BD
import com.example.turismoapp.Framework.dto.PlaceDto
import com.example.turismoapp.Framework.local.dao.DestinationDao // Importar el DAO
import com.example.turismoapp.Framework.local.db.AppDatabase // Importar la Base de Datos
import com.example.turismoapp.Framework.repository.BoliviaRemoteDataSource
import com.example.turismoapp.Framework.repository.DestinationsRepository // Clase concreta
import com.example.turismoapp.Framework.service.RetrofitBuilder
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch // Manejo de errores del Flow
import kotlinx.coroutines.flow.map // Transformación de datos en el Flow
import kotlinx.coroutines.flow.stateIn // Conversión de Flow a StateFlow
import kotlinx.coroutines.launch

// El estado de UI sigue siendo el mismo
sealed interface HomeUiState {
    object Loading : HomeUiState
    data class Success(val places: List<PlaceDto>) : HomeUiState
    data class Error(val message: String) : HomeUiState
}

class HomeViewModel(app: Application) : AndroidViewModel(app) {

    // 1. Inicialización de Room (Base de Datos)
    private val db: AppDatabase by lazy {
        Room.databaseBuilder(
            app.applicationContext,
            AppDatabase::class.java,
            "turismo_database" // Nombre de tu BD
        ).build()
    }

    // 2. Inicialización del DAO
    private val localDao: DestinationDao by lazy { db.destinationDao() }

    // 3. Inicialización del Repositorio CON DAO y Remote
    // Usamos la implementación concreta, inyectándole sus dependencias
    private val repo: DestinationsRepository by lazy {
        val rb = RetrofitBuilder(app.applicationContext)
        DestinationsRepository(
            remote = BoliviaRemoteDataSource(rb.boliviaService),
            localDao = localDao
        )
    }

    // ... (Toda la inicialización del db, localDao, y repo) ...

    // 4. Transformar el Flow de datos (desde Room) en un StateFlow observable por la UI
    val ui: StateFlow<HomeUiState> = repo.getPopularInCochabamba()
        // 1. ✅ ARREGLO FINAL: Usamos map<TipoEntrada, TipoSalida> para forzar la salida a la interfaz HomeUiState
        .map<List<PlaceDto>, HomeUiState> { places ->
            // Aquí solo produces HomeUiState.Success
            if (places.isEmpty()) {
                HomeUiState.Success(getCochabambaPlaces())
            } else {
                val combinedPlaces = places + getCochabambaPlaces()
                HomeUiState.Success(combinedPlaces.distinctBy { it.name })
            }
        }
        .catch { e ->
            // 2. Ahora, como el Flow sabe que su tipo es HomeUiState, puede emitir HomeUiState.Error
            emit(HomeUiState.Error("Error al cargar la caché: ${e.message}"))
        }
        .stateIn(
            scope = viewModelScope,
            initialValue = HomeUiState.Loading,
            started = SharingStarted.WhileSubscribed(5000)
        )

// ... (El resto del ViewModel) ...

// ... (El resto de HomeViewModel.kt) ...

    // ✅ Se ejecuta automáticamente al crear el ViewModel
    init {
        // Al iniciar, intentamos sincronizar la API con la caché de Room
        refreshData()
    }

    // Renombramos la función para clarificar su propósito: forzar la sincronización
    fun load() {
        refreshData()
    }

    // Función que llama al repositorio para SINCRONIZAR la red con Room
    fun refreshData() {
        viewModelScope.launch {
            try {
                // Llama al repositorio para traer de la red y guardar en Room.
                // Room lo guarda, notifica al 'ui' Flow, y la UI se actualiza sola.
                repo.refreshPopularInCochabamba()
            } catch (e: Exception) {
                // Aquí solo manejamos errores de RED. El estado UI se mantiene
                // mostrando la CACHÉ o el estado anterior si la caché está vacía.
                println("Error al sincronizar datos de red: ${e.message}")
            }
        }
    }

    // Tu lista de lugares hardcodeados se mantiene sin cambios
    private fun getCochabambaPlaces() = listOf(
        // ... (Tu lista de PlaceDto estáticos aquí) ...
        PlaceDto(
            id = "cristo_concordia",
            name = "Cristo de la Concordia",
            description = "Estatua monumental de 34.20 metros de altura ubicada en el cerro San Pedro. Es más alta que el Cristo Redentor de Río de Janeiro y ofrece vistas panorámicas de toda la ciudad.",
            city = "Cochabamba",
            department = "Cochabamba",
            rating = 4.8,
            image = "https://www.civitatis.com/f/bolivia/cochabamba/tour-cochabamba-cristo-concordia-589x392.jpg"
        ),
        // ... (El resto de tus 7 PlaceDto) ...
    )
}