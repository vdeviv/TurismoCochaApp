package com.turismoapp.mayuandino.feature.movie.domain.usecase

import com.turismoapp.mayuandino.feature.movie.domain.model.MovieModel
import com.turismoapp.mayuandino.feature.movie.domain.repository.IMoviesRepository

class FetchPopularMoviesUseCase(
    private val movieRepository: IMoviesRepository
) {
    suspend fun invoke(): Result<List<MovieModel>> {
        return movieRepository.fetchPopularMovies()
    }
}