package com.example.turismoapp.feature.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.turismoapp.Framework.repository.BoliviaRemoteDataSource
import com.example.turismoapp.Framework.repository.DestinationsRepository
import com.example.turismoapp.Framework.service.RetrofitBuilder
import com.example.turismoapp.Framework.dto.PlaceDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed interface HomeUiState {
    object Loading : HomeUiState
    data class Success(val places: List<PlaceDto>) : HomeUiState
    data class Error(val message: String) : HomeUiState
}

class HomeViewModel(app: Application) : AndroidViewModel(app) {

    private val repo: DestinationsRepository by lazy {
        val rb = RetrofitBuilder(app.applicationContext)
        DestinationsRepository(BoliviaRemoteDataSource(rb.boliviaService))
    }

    private val _ui = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val ui: StateFlow<HomeUiState> = _ui

    // ✅ Se ejecuta automáticamente al crear el ViewModel
    init {
        load()
    }

    fun load() {
        _ui.value = HomeUiState.Loading
        viewModelScope.launch {
            try {
                // Intentar obtener datos de la API
                val places = repo.popularInCochabamba()

                if (places.isEmpty()) {
                    // Si la API no devuelve nada, usar lugares reales de Cochabamba
                    _ui.value = HomeUiState.Success(getCochabambaPlaces())
                } else {
                    // Combinar datos de API con lugares locales
                    val combinedPlaces = places + getCochabambaPlaces()
                    _ui.value = HomeUiState.Success(combinedPlaces.distinctBy { it.name })
                }

            } catch (e: Exception) {
                e.printStackTrace()
                // En caso de error, mostrar lugares reales de Cochabamba
                _ui.value = HomeUiState.Success(getCochabambaPlaces())
            }
        }
    }

    // ✅ Lugares turísticos REALES de Cochabamba que mencionaste
    private fun getCochabambaPlaces() = listOf(
        PlaceDto(
            id = "cristo_concordia",
            name = "Cristo de la Concordia",
            description = "Estatua monumental de 34.20 metros de altura ubicada en el cerro San Pedro. Es más alta que el Cristo Redentor de Río de Janeiro y ofrece vistas panorámicas de toda la ciudad.",
            city = "Cochabamba",
            department = "Cochabamba",
            rating = 4.8,
            image = "https://images.unsplash.com/photo-1583224964723-aca5ff12fc50?w=800&q=80"
        ),
        PlaceDto(
            id = "plaza_14_septiembre",
            name = "Plaza 14 de Septiembre",
            description = "Plaza principal de Cochabamba, centro histórico y cultural de la ciudad. Rodeada de edificios coloniales, la Catedral Metropolitana y jardines bien cuidados.",
            city = "Cochabamba",
            department = "Cochabamba",
            rating = 4.6,
            image = "https://images.unsplash.com/photo-1555109307-f7d9da25c244?w=800&q=80"
        ),
        PlaceDto(
            id = "centro_simon_patino",
            name = "Centro Simón I. Patiño",
            description = "Complejo cultural que incluye un palacio de estilo neoclásico francés, jardines y espacios para eventos culturales. Patrimonio histórico de Bolivia.",
            city = "Cochabamba",
            department = "Cochabamba",
            rating = 4.7,
            image = "https://images.unsplash.com/photo-1564501049412-61c2a3083791?w=800&q=80"
        ),
        PlaceDto(
            id = "convento_santa_teresa",
            name = "Convento Santa Teresa",
            description = "Convento colonial del siglo XVIII con arquitectura barroca. Museo religioso con arte sacro, pinturas y objetos históricos de la época colonial.",
            city = "Cochabamba",
            department = "Cochabamba",
            rating = 4.5,
            image = "https://images.unsplash.com/photo-1609137144813-7d9921338f24?w=800&q=80"
        ),
        PlaceDto(
            id = "parque_familia",
            name = "Parque de la Familia",
            description = "Amplio parque recreativo ideal para actividades familiares, con áreas verdes, juegos infantiles, canchas deportivas y espacios para picnic.",
            city = "Cochabamba",
            department = "Cochabamba",
            rating = 4.4,
            image = "https://images.unsplash.com/photo-1571019613576-2b22c76fd955?w=800&q=80"
        ),
        PlaceDto(
            id = "laguna_alalay",
            name = "Laguna Alalay",
            description = "Hermosa laguna natural en el corazón de la ciudad, perfecta para caminatas, observación de aves y disfrutar de atardeceres espectaculares.",
            city = "Cochabamba",
            department = "Cochabamba",
            rating = 4.5,
            image = "https://images.unsplash.com/photo-1439066615861-d1af74d74000?w=800&q=80"
        ),
        PlaceDto(
            id = "palacio_portales",
            name = "Palacio Portales",
            description = "Majestuosa mansión de estilo francés construida en 1927, ahora convertida en museo cultural. Destacan sus jardines y arquitectura europea.",
            city = "Cochabamba",
            department = "Cochabamba",
            rating = 4.6,
            image = "https://images.unsplash.com/photo-1582268611958-ebfd161ef9cf?w=800&q=80"
        ),
        PlaceDto(
            id = "parque_tunari",
            name = "Parque Nacional Tunari",
            description = "Área protegida de 3,090 km² con ecosistemas de alta montaña, lagunas glaciares y biodiversidad única. Ideal para trekking y montañismo.",
            city = "Cochabamba",
            department = "Cochabamba",
            rating = 4.9,
            image = "https://images.unsplash.com/photo-1506905925346-21bda4d32df4?w=800&q=80"
        )
    )
}