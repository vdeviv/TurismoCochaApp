package com.turismoapp.mayuandino.framework.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "calendar_events")
data class CalendarEventEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val location: String?,
    val price: Double,
    val date: String,
    val description: String?,
    val imageUrl: String
)
