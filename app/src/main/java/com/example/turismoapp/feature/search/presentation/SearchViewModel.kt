package com.example.turismoapp.feature.search.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.turismoapp.Framework.dto.PlaceDto
import com.example.turismoapp.Framework.repository.BoliviaRemoteDataSource
import com.example.turismoapp.Framework.repository.DestinationsRepository
import com.example.turismoapp.Framework.service.RetrofitBuilder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SearchViewModel(app: Application) : AndroidViewModel(app) {

    private val repo: DestinationsRepository by lazy {
        val rb = RetrofitBuilder(app.applicationContext)
        DestinationsRepository(BoliviaRemoteDataSource(rb.boliviaService))
    }

    private val _allPlaces = MutableStateFlow<List<PlaceDto>>(emptyList())
    val allPlaces: StateFlow<List<PlaceDto>> = _allPlaces

    init {
        loadAllPlaces()
    }

    private fun loadAllPlaces() {
        viewModelScope.launch {
            try {
                val apiPlaces = repo.popularInCochabamba()
                val localPlaces = getLocalPlaces()

                val combined = (apiPlaces + localPlaces).distinctBy { it.id }
                _allPlaces.value = combined

            } catch (e: Exception) {
                _allPlaces.value = getLocalPlaces()
            }
        }
    }

    // REUTILIZO LOS MISMOS LUGARES QUE TIENES EN HomeViewModel
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
        ),
        PlaceDto(
            id = "laguna_alalay",
            name = "Laguna Alalay",
            description = "Laguna natural en el corazón de la ciudad.",
            city = "Cochabamba",
            department = "Cochabamba",
            rating = 4.5,
            image = "https://www.opinion.com.bo/asset/thumbnail,992,558,center,center/media/opinion/images/2023/03/12/2023031220545699714.jpg",
            latitude = -17.403204,
            longitude = -66.145481
        )
    )
}
