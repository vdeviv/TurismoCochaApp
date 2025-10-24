package com.example.turismoapp.Framework.service

import com.example.turismoapp.Framework.dto.PlaceResponseDto
import retrofit2.http.GET
import retrofit2.http.Path

interface IBoliviaService {

    @GET("/api/v1/cities/{city}/places")
    suspend fun getPlacesByCity(
        @Path("city") city: String
    ): PlaceResponseDto
}
