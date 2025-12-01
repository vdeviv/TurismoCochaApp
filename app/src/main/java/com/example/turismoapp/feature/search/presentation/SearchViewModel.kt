package com.example.turismoapp.feature.search.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.turismoapp.framework.dto.PlaceDto
import com.example.turismoapp.framework.local.db.AppDatabase
import com.example.turismoapp.feature.home.data.FirebaseHomeService
import com.example.turismoapp.feature.home.data.HomeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SearchViewModel(app: Application) : AndroidViewModel(app) {

    private val db: AppDatabase by lazy {
        AppDatabase.getInstance(app.applicationContext)
    }

    // Usamos el mismo HomeRepository que HomeScreen
    private val repo by lazy {
        HomeRepository(
            firebase = FirebaseHomeService(),
            db = db
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
                // 1. Firebase + Room híbrido
                val firebaseAndLocal = repo.getHomePlaces()

                // 2. Añadimos lugares estáticos/mocks si quieres
                val finalList = (firebaseAndLocal + getLocalPlaces())
                    .distinctBy { it.id }

                _allPlaces.value = finalList

            } catch (e: Exception) {
                // Si Firebase falla → solo Room + locales
                val cached = repo.getHomePlaces()
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
            rating = "4.8",
            imageUrl = "https://www.civitatis.com/f/bolivia/cochabamba/tour-cochabamba-cristo-concordia-589x392.jpg",
            latitude = -17.376000,
            longitude = -66.156818
        ),
        PlaceDto(
            id = "palacio_portales",
            name = "Palacio Portales",
            description = "Mansión de estilo francés...",
            city = "Cochabamba",
            department = "Cochabamba",
            rating = "4.6",
            imageUrl = "https://www.opinion.com.bo/asset/thumbnail,992,558,center,center/media/opinion/images/2024/10/21/2024102122033078671.jpg",
            latitude = -17.374950,
            longitude = -66.164116
        )
    )
}
