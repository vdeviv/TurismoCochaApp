package com.turismoapp.mayuandino.feature.packages.domain.model

data class PackageModel(
    val id: String,
    val title: String,
    val imageUrl: String,
    val rating: Double,
    val description: String,
    val city: String
)
