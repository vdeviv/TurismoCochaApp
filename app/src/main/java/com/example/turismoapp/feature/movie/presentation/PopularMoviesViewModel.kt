package com.example.turismoapp.feature.movie.presentation


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.turismoapp.feature.movie.domain.model.MovieModel
import com.example.turismoapp.feature.movie.domain.usecase.FetchPopularMoviesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PopularMoviesViewModel(
    private val fetchPopularMovies: FetchPopularMoviesUseCase
): ViewModel() {

    sealed class UiState {
        object Loading : UiState()
        data class Success(val movies: List<MovieModel>) : UiState()
        data class Error(val message: String) : UiState()
    }

    private val _state = MutableStateFlow<UiState>(UiState.Loading)
    val state: StateFlow<UiState> = _state.asStateFlow()

    fun fetchPopularMovies() {
        viewModelScope.launch(Dispatchers.IO) {
            _state.value = UiState.Loading
            val result = fetchPopularMovies.invoke()
            result.fold(
                onSuccess = {
                    _state.value = UiState.Success(it)
                },
                onFailure = {
                    _state.value = UiState.Error("error")
                }
            )
        }
    }
}