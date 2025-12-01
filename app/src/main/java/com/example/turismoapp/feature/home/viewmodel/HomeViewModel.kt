package com.example.turismoapp.feature.home.viewmodel

import android.app.Application
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

    init { loadPlaces() }

    fun refresh() = loadPlaces()

    private fun loadPlaces() {
        viewModelScope.launch {
            _ui.value = HomeUiState.Loading

            try {
                val list = repository.getHomePlaces()
                _ui.value = HomeUiState.Success(list)
            } catch (e: Exception) {
                _ui.value = HomeUiState.Error("Error: ${e.message}")
            }
        }
    }
}
