package com.example.turismoapp.feature.home.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.turismoapp.framework.dto.PlaceDto
import com.example.turismoapp.feature.home.data.FirebaseHomeService
import com.example.turismoapp.feature.home.data.HomeRepository
import com.example.turismoapp.framework.local.db.AppDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed interface HomeUiState {
    object Loading : HomeUiState
    data class Success(val places: List<PlaceDto>) : HomeUiState
    data class Error(val message: String) : HomeUiState
}

class HomeViewModel(app: Application) : AndroidViewModel(app) {

    private val db = AppDatabase.getInstance(app)
    private val firebaseService = FirebaseHomeService()
    private val repository = HomeRepository(firebaseService, db)

    private val _ui = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val ui: StateFlow<HomeUiState> = _ui

    init {
        Log.d("HOME_VM", "HomeViewModel inicializado")
        loadPlaces()
    }

    fun refresh() {
        Log.d("HOME_VM", "Refrescando datos...")
        loadPlaces()
    }

    private fun loadPlaces() {
        viewModelScope.launch {
            Log.d("HOME_VM", "Cargando lugares...")
            _ui.value = HomeUiState.Loading

            try {
                val list = repository.getHomePlaces()

                Log.d("HOME_VM", "Lugares obtenidos: ${list.size}")
                list.forEachIndexed { index, place ->
                    Log.d("HOME_VM", "  [$index] ${place.name} - ${place.city}")
                }

                if (list.isEmpty()) {
                    Log.w("HOME_VM", "️Lista vacía - verificar Firebase y reglas de seguridad")
                    _ui.value = HomeUiState.Error("No se encontraron lugares")
                } else {
                    _ui.value = HomeUiState.Success(list)
                }

            } catch (e: Exception) {
                Log.e("HOME_VM", "Error cargando lugares: ${e.message}", e)
                _ui.value = HomeUiState.Error("Error: ${e.message}")
            }
        }
    }
}