package com.example.turismoapp.feature.movie.data.datasource

import  com.example.turismoapp.feature.movie.data.api.MovieService
import  com.example.turismoapp.feature.movie.domain.model.MovieModel

class MovieRemoteDataSource(
    private val movieServie: MovieService,
    private val apiKey: String
) {
    suspend fun fetchPopularMovies(): Result<List<MovieModel>> {
        val response = movieServie.fetchPopularMovies(apiKey = apiKey)
        return if (response.isSuccessful) {
            val moviePage = response.body()
            if (moviePage != null) {
                return Result.success(moviePage.results.map { dto ->  MovieModel("https://image.tmdb.org/t/p/w185"+dto.pathUrl, dto.title) } )
            }
            Result.success(emptyList())
        } else {
            Result.failure(Exception("Error"))
        }
    }
}