package com.example.turismoapp.Framework.repository

import com.example.turismoapp.Framework.dto.PlaceDto

class DestinationsRepository(private val ds: BoliviaRemoteDataSource) {
    suspend fun popularInCochabamba(): List<PlaceDto> {
        val items = ds.getPlacesByDepartment("Cochabamba")
        return items.map { it.copy(rating = it.rating ?: (3.5..5.0).random()) }
    }

    private fun ClosedFloatingPointRange<Double>.random() =
        (start + Math.random() * (endInclusive - start))
}
