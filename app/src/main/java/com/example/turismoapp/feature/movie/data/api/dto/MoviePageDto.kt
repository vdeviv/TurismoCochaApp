package com.example.turismoapp.feature.movie.data.api.dto

data class MoviePageDto(
    val page: Int,
    val results: List<MovieDto>
)
