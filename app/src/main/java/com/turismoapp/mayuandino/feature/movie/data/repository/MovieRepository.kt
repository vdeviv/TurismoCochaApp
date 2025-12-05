package com.turismoapp.mayuandino.feature.movie.data.repository

import com.turismoapp.mayuandino.feature.movie.data.datasource.MovieRemoteDataSource
import com.turismoapp.mayuandino.feature.movie.domain.model.MovieModel
import com.turismoapp.mayuandino.feature.movie.domain.repository.IMoviesRepository

class MovieRepository(
    private val movieRemoteDataSource: MovieRemoteDataSource
): IMoviesRepository {
    override suspend fun fetchPopularMovies(): Result<List<MovieModel>>
            = movieRemoteDataSource.fetchPopularMovies()

}