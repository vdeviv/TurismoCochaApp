package com.example.turismoapp.Framework.repository

import com.example.turismoapp.Framework.dto.PlaceDto
import com.example.turismoapp.Framework.service.IBoliviaService

class BoliviaRemoteDataSource(
    private val api: IBoliviaService
) {
    suspend fun getPlacesInCochabamba(): List<PlaceDto> {
        val response = api.getPlacesByCity("cochabamba")
        return response.data ?: emptyList()
    }
}
