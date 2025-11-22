package com.example.turismoapp.Framework.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "calendar_events")
data class CalendarEventEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val title: String,
    val location: String?,
    val price: Double,
    val date: String,           // yyyy-MM-dd
    val description: String? = null
)
