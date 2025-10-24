package com.example.turismoapp.Framework.service

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

// DTO para la respuesta de lugares
data class OpenTripMapPlace(
    val xid: String,
    val name: String,
    val kinds: String,
    val dist: Double,
    val point: Point
)

data class Point(
    val lon: Double,
    val lat: Double
)

// DTO para detalles de un lugar
data class PlaceDetails(
    val xid: String,
    val name: String,
    val kinds: String,
    val rate: Int, // 1-7
    val image: String?,
    val preview: Preview?,
    val wikipedia: String?,
    val wikipedia_extracts: WikiExtract?,
    val point: Point,
    val address: Address?
)

data class Preview(
    val source: String?,
    val height: Int?,
    val width: Int?
)

data class WikiExtract(
    val title: String?,
    val text: String?,
    val html: String?
)

data class Address(
    val city: String?,
    val road: String?,
    val state: String?,
    val country: String?
)

interface IBoliviaService {

    // Buscar lugares en un radio (usamos el mismo nombre de interfaz)
    @GET("en/places/radius")
    suspend fun getPlacesByRadius(
        @Query("radius") radius: Int = 10000,
        @Query("lon") longitude: Double,
        @Query("lat") latitude: Double,
        @Query("rate") minRate: Int = 2,
        @Query("limit") limit: Int = 50,
        @Query("apikey") apiKey: String
    ): List<OpenTripMapPlace>

    // Obtener detalles de un lugar específico
    @GET("en/places/xid/{xid}")
    suspend fun getPlaceDetails(
        @Path("xid") xid: String,
        @Query("apikey") apiKey: String
    ): PlaceDetails
}