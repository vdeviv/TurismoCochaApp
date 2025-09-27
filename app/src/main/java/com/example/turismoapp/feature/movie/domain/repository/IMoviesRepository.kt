package com.example.turismoapp.feature.movie.domain.repository

import com.example.turismoapp.feature.movie.domain.model.MovieModel

interface IMoviesRepository {
    suspend fun fetchPopularMovies(): Result<List<MovieModel>>
}