package com.example.turismoapp.feature.movie.domain.usecase

import com.example.turismoapp.feature.movie.domain.model.MovieModel
import com.example.turismoapp.feature.movie.domain.repository.IMoviesRepository

class FetchPopularMoviesUseCase(
    private val movieRepository: IMoviesRepository
) {
    suspend fun invoke(): Result<List<MovieModel>> {
        return movieRepository.fetchPopularMovies()
    }
}