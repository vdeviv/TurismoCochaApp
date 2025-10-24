package com.example.turismoapp.Framework.service

import com.example.turismoapp.Framework.dto.PlaceResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface IBoliviaService {
    @GET("/api/v1/places")
    suspend fun getPlacesByDepartment(
        @Query("department") department: String = "Cochabamba"
    ): Response<PlaceResponseDto>
}
