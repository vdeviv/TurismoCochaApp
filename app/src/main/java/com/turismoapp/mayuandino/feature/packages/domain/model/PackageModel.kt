package com.turismoapp.mayuandino.feature.packages.domain.model

data class PackageModel(
    val id: String = "",
    val title: String = "",
    val imageUrl: String = "",
    val rating: Double = 0.0,
    val city: String = "",
    val description: String = "",
    val joinedUsers: List<String> = emptyList()
)
