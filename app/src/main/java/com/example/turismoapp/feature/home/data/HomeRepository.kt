package com.example.turismoapp.feature.home.data

import com.example.turismoapp.framework.dto.PlaceDto
import com.example.turismoapp.framework.local.db.AppDatabase
import com.example.turismoapp.framework.local.mapper.toDto
import com.example.turismoapp.framework.local.mapper.toEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.flow.first


class HomeRepository(
    private val firebase: FirebaseHomeService,
    private val db: AppDatabase
) {

    private val dao = db.destinationDao()

    private suspend fun fetchFromFirebase(): List<PlaceDto> {
        return firebase.getHomePlaces()
    }

    private suspend fun saveToLocal(list: List<PlaceDto>) {
        dao.insertPlaces(list.map { it.toEntity() })
    }

    private suspend fun getFromLocal(): List<PlaceDto> {
        return dao.getAllPlaces()
            .first()
            .map { it.toDto() }
    }

    suspend fun getHomePlaces(): List<PlaceDto> = withContext(Dispatchers.IO) {

        try {
            val remote = fetchFromFirebase()

            if (remote.isNotEmpty()) {
                saveToLocal(remote)
                return@withContext remote
            }

            return@withContext getFromLocal()

        } catch (e: Exception) {
            val local = getFromLocal()
            if (local.isNotEmpty()) return@withContext local

            throw Exception("Sin datos disponibles")
        }
    }
}
