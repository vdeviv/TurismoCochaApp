package com.turismoapp.mayuandino.feature.notification.presentation

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.turismoapp.mayuandino.feature.notification.data.model.LocalNotification
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


class NotificationViewModel(app: Application) : AndroidViewModel(app) {
    private val prefs = app.getSharedPreferences("noti_prefs", MODE_PRIVATE)
    private val gson = Gson()
    private val type = object : TypeToken<MutableList<LocalNotification>>() {}.type

    private val _notifications = MutableStateFlow<List<LocalNotification>>(emptyList())
    val notifications: StateFlow<List<LocalNotification>> = _notifications

    init {
        loadNotifications()
    }

    private fun loadNotifications() {
        // Asegúrate de que la clave 'notifications_list' coincida
        val currentJson = prefs.getString("notifications_list", "[]")

        // Esta línea puede fallar si el formato JSON es incorrecto o si falta Gson
        val list: List<LocalNotification> = gson.fromJson(currentJson, type) ?: emptyList()

        // 💡 Añade un Log aquí para DEBUG:
        Log.d("NOTI_VM", "Notificaciones cargadas: ${list.size}")

        _notifications.value = list
    }

    fun clearAllNotifications() {
        prefs.edit().putString("notifications_list", "[]").apply()
        _notifications.value = emptyList() // Vaciar la lista en el estado
        Log.d("NOTI_LOCAL", "Historial de notificaciones borrado.")
    }

    class NotificationViewModelFactory(private val application: Application) : androidx.lifecycle.ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(NotificationViewModel::class.java)) {
                // Retorna la instancia de tu ViewModel
                return NotificationViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}