package com.example.turismoapp.feature.movie.data.api

import com.example.turismoapp.feature.movie.data.api.dto.MoviePageDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieService {

    @GET("/3/discover/movie" )
    suspend fun fetchPopularMovies(
        @Query("sort_by") sortBy : String = "popularity.desc",
        @Query("api_key") apiKey : String) : Response<MoviePageDto>
}