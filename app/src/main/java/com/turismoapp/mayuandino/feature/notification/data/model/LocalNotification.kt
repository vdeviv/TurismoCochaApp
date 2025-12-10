package com.turismoapp.mayuandino.feature.notification.data.model

data class LocalNotification(
    val id: Long = System.currentTimeMillis(),
    val title: String,
    val body: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false
)