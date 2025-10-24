package com.example.turismoapp.Framework.repository

import android.util.Log
import com.example.turismoapp.Framework.dto.PlaceDto
import com.example.turismoapp.Framework.service.IBoliviaService
import com.example.turismoapp.Framework.service.RetrofitBuilder

class BoliviaRemoteDataSource(
    private val api: IBoliviaService
) {
    // Coordenadas de Cochabamba, Bolivia
    private val COCHABAMBA_LAT = -17.3935
    private val COCHABAMBA_LON = -66.1570

    suspend fun getPlacesInCochabamba(): List<PlaceDto> {
        return try {
            Log.d("API_CALL", "🌍 Buscando lugares en Cochabamba...")

            // 1. Obtener lista de lugares cercanos a Cochabamba
            val places = api.getPlacesByRadius(
                radius = 15000, // 15km de radio desde el centro
                longitude = COCHABAMBA_LON,
                latitude = COCHABAMBA_LAT,
                minRate = 2, // Rating mínimo de 2/7
                limit = 20,
                apiKey = RetrofitBuilder.OPENTRIPMAP_API_KEY
            )

            Log.d("API_CALL", "✅ Encontrados ${places.size} lugares")

            // 2. Obtener detalles de cada lugar (máximo 10 para no saturar)
            val detailedPlaces = places.take(10).mapNotNull { place ->
                try {
                    Log.d("API_CALL", "📍 Obteniendo detalles de: ${place.name}")

                    val details = api.getPlaceDetails(
                        xid = place.xid,
                        apiKey = RetrofitBuilder.OPENTRIPMAP_API_KEY
                    )

                    // Solo incluir lugares que tengan nombre
                    if (details.name.isNotEmpty()) {
                        PlaceDto(
                            id = details.xid,
                            name = details.name,
                            description = details.wikipedia_extracts?.text?.take(200) ?:
                            "Lugar turístico en Cochabamba",
                            city = details.address?.city ?: "Cochabamba",
                            department = "Cochabamba",
                            image = details.preview?.source ?: details.image ?: "",
                            rating = convertRateToStars(details.rate)
                        )
                    } else {
                        null
                    }
                } catch (e: Exception) {
                    Log.e("API_CALL", "❌ Error obteniendo detalles de ${place.name}: ${e.message}")
                    null
                }
            }

            Log.d("API_CALL", "🎉 Procesados ${detailedPlaces.size} lugares con éxito")
            detailedPlaces

        } catch (e: Exception) {
            Log.e("API_CALL", "💥 Error al llamar a OpenTripMap API", e)
            Log.e("API_CALL", "Mensaje: ${e.message}")
            Log.e("API_CALL", "Tipo: ${e.javaClass.simpleName}")
            emptyList()
        }
    }

    // Convierte el rate de OpenTripMap (1-7) a estrellas (1-5)
    private fun convertRateToStars(rate: Int): Double {
        return when (rate) {
            1 -> 2.5
            2 -> 3.0
            3 -> 3.5
            4 -> 4.0
            5 -> 4.3
            6 -> 4.6
            7 -> 5.0
            else -> 3.5
        }
    }
}
