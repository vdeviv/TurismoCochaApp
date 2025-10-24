package com.example.turismoapp.Framework.dto

data class PlaceResponseDto(
    val success: Boolean,
    val data: List<PlaceDto>
)

data class PlaceDto(
    val id: Int,
    val name: String,
    val description: String? = null,
    val image: String? = null,
    val department: String,
    val city: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val rating: Double? = null
)
