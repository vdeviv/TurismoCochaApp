package com.turismoapp.mayuandino.framework.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "places")
data class DestinationEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val rating: Double,
    val city: String,
    val department: String,
    val imageUrl: String,
    val latitude: Double,
    val longitude: Double
)
