package com.turismoapp.mayuandino.feature.movie.domain.repository

import com.turismoapp.mayuandino.feature.movie.domain.model.MovieModel

interface IMoviesRepository {
    suspend fun fetchPopularMovies(): Result<List<MovieModel>>
}