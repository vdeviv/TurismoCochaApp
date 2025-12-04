package com.example.turismoapp.feature.home.data

import android.util.Log
import com.example.turismoapp.framework.dto.PlaceDto
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirebaseHomeService {

    private val firestore = FirebaseFirestore.getInstance()

    suspend fun getHomePlaces(): List<PlaceDto> {
        return try {
            val snapshot = firestore.collection("places")
                .get()
                .await()

            val list = snapshot.documents.map { doc ->
                PlaceDto(
                    id = doc.id,
                    name = doc.getString("name") ?: "Sin nombre",
                    description = doc.getString("description") ?: "",
                    rating = doc.getDouble("rating") ?: 0.0, // ✅ Cambiado a getDouble()
                    city = doc.getString("city") ?: "",
                    department = doc.getString("department") ?: "",
                    imageUrl = doc.getString("imageUrl") ?: "",
                    latitude = doc.getDouble("latitude") ?: 0.0,
                    longitude = doc.getDouble("longitude") ?: 0.0
                )
            }

            Log.d("FIREBASE_DEBUG", "✅ Se encontraron ${list.size} lugares en Firestore")
            list

        } catch (e: Exception) {
            Log.e("FIREBASE_ERROR", "❌ Error leyendo Firebase: ${e.message}", e)
            emptyList()
        }
    }
}