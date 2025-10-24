package com.example.turismoapp.Framework.repository

import com.example.turismoapp.Framework.dto.PlaceDto

class DestinationsRepository(
    private val remote: BoliviaRemoteDataSource
) {
    suspend fun popularInCochabamba(): List<PlaceDto> {
        return remote.getPlacesInCochabamba()
    }
}
