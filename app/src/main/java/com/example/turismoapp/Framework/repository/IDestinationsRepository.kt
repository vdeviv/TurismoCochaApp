package com.example.turismoapp.Framework.repository

import com.example.turismoapp.Framework.dto.PlaceDto
import kotlinx.coroutines.flow.Flow

interface IDestinationsRepository {
    fun getPopularInCochabamba(): Flow<List<PlaceDto>>
}