package com.example.turismoapp.Framework.repository

// ... otras importaciones ...
import com.example.turismoapp.Framework.dto.PlaceDto
import com.example.turismoapp.Framework.local.dao.DestinationDao
import com.example.turismoapp.Framework.local.entity.DestinationEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map // Necesario para mapear

class DestinationsRepository(
    private val remote: BoliviaRemoteDataSource,
    private val localDao: DestinationDao // <-- NUEVO: Inyectamos el DAO de Room
) : IDestinationsRepository {

    // 1. Nueva función para OBTENER DATOS (usando Room como caché)
    override fun getPopularInCochabamba(): Flow<List<PlaceDto>> {
        // Usamos Flow del DAO y lo mapeamos de Entity a Dto
        return localDao.getAllDestinations().map { entities ->
            entities.map { it.toPlaceDto() } // Conversión Entity -> Dto
        }
    }

    // 2. Nueva función para SINCRONIZAR DATOS (llamada desde el ViewModel)
    suspend fun refreshPopularInCochabamba() {
        try {
            // 1. Obtener de la red
            val remotePlaces = remote.getPlacesInCochabamba()

            // 2. Convertir de DTO (red) a Entity (Room)
            val entitiesToCache = remotePlaces.map { it.toDestinationEntity() }

            // 3. Insertar en la BD (Room)
            localDao.insertAll(entitiesToCache)
        } catch (e: Exception) {
            // Manejo de error si falla la red, pero mantenemos la caché existente
            println("Error al sincronizar datos de red: ${e.message}")
            throw e // Propagar la excepción para que el ViewModel la maneje
        }
    }
}

// Funciones de mapeo (pueden ir en archivos separados, pero por simplicidad, aquí)
fun DestinationEntity.toPlaceDto() = PlaceDto(
    id = this.id.toString(), // Room ID a String
    name = this.name,
    description = this.description,
    city = this.city,
    department = "Cochabamba", // Asumo que siempre es Cochabamba
    image = this.imageUrl ?: "",
    rating = this.rating ?: 0.0,
    latitude = -17.392, // Opcional: añadir lat/lon a DestinationEntity
    longitude = -66.159
)

fun PlaceDto.toDestinationEntity() = DestinationEntity(
    name = this.name,
    description = this.description,
    city = this.city,
    imageUrl = this.image,
    rating = this.rating
    // id se deja en 0L para que Room lo autogenere, o usar el id de PlaceDto
    // (si el id de PlaceDto es un String, se complica el mapping)
)
