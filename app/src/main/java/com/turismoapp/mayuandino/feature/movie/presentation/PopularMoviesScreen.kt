package com.turismoapp.mayuandino.feature.movie.presentation

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import org.koin.androidx.compose.koinViewModel

@Composable
fun PopularMoviesScreen(
    popularMoviesViewModel: PopularMoviesViewModel = koinViewModel()
) {
    val state = popularMoviesViewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        popularMoviesViewModel.fetchPopularMovies()
    }

    when (val s = state.value) {
        is PopularMoviesViewModel.UiState.Error -> {
            Text(s.message)
        }
        is PopularMoviesViewModel.UiState.Loading ->
            CircularProgressIndicator()
        is PopularMoviesViewModel.UiState.Success ->
            PopularMoviesView(movies = s.movies)

    }

}