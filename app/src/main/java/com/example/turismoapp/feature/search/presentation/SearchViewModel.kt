package com.example.turismoapp.feature.search.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.turismoapp.Framework.dto.PlaceDto
import com.example.turismoapp.Framework.local.db.AppDatabase
import com.example.turismoapp.Framework.repository.BoliviaRemoteDataSource
import com.example.turismoapp.Framework.repository.DestinationsRepository
import com.example.turismoapp.Framework.service.RetrofitBuilder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SearchViewModel(app: Application) : AndroidViewModel(app) {

    private val db: AppDatabase by lazy {
        Room.databaseBuilder(
            app.applicationContext,
            AppDatabase::class.java,
            "turismo_database"
        ).build()
    }

    private val localDao by lazy { db.destinationDao() }

    private val repo: DestinationsRepository by lazy {
        val rb = RetrofitBuilder(app.applicationContext)
        DestinationsRepository(
            remote = BoliviaRemoteDataSource(rb.boliviaService),
            localDao = localDao
        )
    }

    private val _allPlaces = MutableStateFlow<List<PlaceDto>>(emptyList())
    val allPlaces: StateFlow<List<PlaceDto>> = _allPlaces

    init {
        loadPlaces()
    }

    private fun loadPlaces() {
        viewModelScope.launch {
            try {
                // 1. Sincronizar red -> Room
                repo.refreshPopularInCochabamba()

                // 2. Obtener datos locales desde Room
                val localPlaces = repo.getPopularInCochabamba().first()

                // 3. Combinar con tus lugares locales manuales
                val combined = (localPlaces + getLocalPlaces()).distinctBy { it.name }

                _allPlaces.value = combined

            } catch (e: Exception) {
                // Si hay error de red, cargar solo Room + locales
                val cached = repo.getPopularInCochabamba().first()
                _allPlaces.value = cached + getLocalPlaces()
            }
        }
    }

    private fun getLocalPlaces() = listOf(
        PlaceDto(
            id = "cristo_concordia",
            name = "Cristo de la Concordia",
            description = "Estatua monumental...",
            city = "Cochabamba",
            department = "Cochabamba",
            rating = 4.8,
            image = "https://www.civitatis.com/f/bolivia/cochabamba/tour-cochabamba-cristo-concordia-589x392.jpg",
            latitude = -17.376000,
            longitude = -66.156818
        ),
        PlaceDto(
            id = "palacio_portales",
            name = "Palacio Portales",
            description = "Mansión de estilo francés...",
            city = "Cochabamba",
            department = "Cochabamba",
            rating = 4.6,
            image = "https://www.opinion.com.bo/asset/thumbnail,992,558,center,center/media/opinion/images/2024/10/21/2024102122033078671.jpg",
            latitude = -17.374950,
            longitude = -66.164116
        )
    )
}
