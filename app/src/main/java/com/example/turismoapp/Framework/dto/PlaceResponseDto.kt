package com.example.turismoapp.Framework.dto

data class PlaceResponseDto(
    val data: List<PlaceDto>?
)

data class PlaceDto(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val city: String = "",
    val department: String = "",
    val image: String = "",
    val rating: Double = 4.7
)