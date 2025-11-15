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
    val rating: Double = 0.0,
    val latitude: Double = -17.392,
    val longitude: Double = -66.159
)