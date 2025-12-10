package com.turismoapp.mayuandino.feature.notification.presentation

// Archivo: NotificationViewModelFactory.kt

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class NotificationViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotificationViewModel::class.java)) {
            // Retorna la instancia de tu ViewModel
            return NotificationViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}