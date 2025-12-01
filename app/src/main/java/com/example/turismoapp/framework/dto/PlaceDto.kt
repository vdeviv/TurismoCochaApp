package com.example.turismoapp.framework.dto

import com.google.firebase.firestore.PropertyName

data class PlaceDto(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val rating: String = "",
    val city: String = "",
    val department: String = "",
    @get:PropertyName("imageUrl")
    @set:PropertyName("imageUrl")
    var imageUrl: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
)