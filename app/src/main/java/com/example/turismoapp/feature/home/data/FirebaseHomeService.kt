package com.example.turismoapp.feature.home.data

import com.example.turismoapp.framework.dto.PlaceDto
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirebaseHomeService {

    private val firestore = FirebaseFirestore.getInstance()

    suspend fun getHomePlaces(): List<PlaceDto> {
        return try {
            firestore.collection("places")
                .get()
                .await()
                .toObjects(PlaceDto::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }
}

