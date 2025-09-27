package com.example.turismoapp.feature.movie.data.api.dto

import com.google.gson.annotations.SerializedName

data class MovieDto(
    @SerializedName("poster_path")
    val pathUrl: String,
    val title: String
)