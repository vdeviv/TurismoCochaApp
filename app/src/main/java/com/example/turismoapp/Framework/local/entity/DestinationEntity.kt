package com.example.turismoapp.Framework.local.entity


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "destinations")
data class DestinationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    val name: String,
    val description: String,
    val city: String,
    val imageUrl: String?,       // si usas imágenes remotas
    val rating: Double?,         // si tu backend o API te da puntuaciones
    val category: String? = null
)