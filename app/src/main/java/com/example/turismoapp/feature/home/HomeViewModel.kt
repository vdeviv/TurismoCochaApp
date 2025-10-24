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

    fun load() {
        _ui.value = HomeUiState.Loading
        viewModelScope.launch {
            try {
                val places = repo.popularInCochabamba()
                if (places.isEmpty()) {
                    _ui.value = HomeUiState.Error("Sin resultados por ahora")
                } else {
                    _ui.value = HomeUiState.Success(places)
                }
            } catch (e: Exception) {
                _ui.value = HomeUiState.Error("Error al conectar con la API")
            }
        }
    }

}
