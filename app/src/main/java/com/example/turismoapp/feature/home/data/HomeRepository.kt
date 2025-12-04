package com.example.turismoapp.feature.home.data

import android.util.Log
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
        Log.d("HOME_REPO", "📡 Intentando obtener datos de Firebase...")
        val result = firebase.getHomePlaces()
        Log.d("HOME_REPO", "📡 Firebase retornó ${result.size} lugares")
        return result
    }

    private suspend fun saveToLocal(list: List<PlaceDto>) {
        Log.d("HOME_REPO", "💾 Guardando ${list.size} lugares en Room...")
        dao.insertPlaces(list.map { it.toEntity() })
        Log.d("HOME_REPO", "✅ Lugares guardados en Room")
    }

    private suspend fun getFromLocal(): List<PlaceDto> {
        Log.d("HOME_REPO", "📂 Obteniendo datos de Room...")
        val result = dao.getAllPlaces()
            .first()
            .map { it.toDto() }
        Log.d("HOME_REPO", "📂 Room retornó ${result.size} lugares")
        return result
    }

    suspend fun getHomePlaces(): List<PlaceDto> = withContext(Dispatchers.IO) {
        try {
            Log.d("HOME_REPO", "🔄 Iniciando carga de lugares...")
            val remote = fetchFromFirebase()

            if (remote.isNotEmpty()) {
                Log.d("HOME_REPO", "✅ Usando datos de Firebase")
                saveToLocal(remote)
                return@withContext remote
            }

            Log.w("HOME_REPO", "⚠️ Firebase vacío, intentando Room...")
            return@withContext getFromLocal()

        } catch (e: Exception) {
            Log.e("HOME_REPO", "❌ Error en Firebase: ${e.message}", e)

            val local = getFromLocal()
            if (local.isNotEmpty()) {
                Log.d("HOME_REPO", "✅ Usando datos de Room (fallback)")
                return@withContext local
            }

            Log.e("HOME_REPO", "❌ Sin datos disponibles")
            throw Exception("Sin datos disponibles: ${e.message}")
        }
    }
}