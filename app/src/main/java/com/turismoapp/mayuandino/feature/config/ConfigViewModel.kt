package com.turismoapp.mayuandino.feature.config

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.remoteconfig.BuildConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.turismoapp.mayuandino.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ConfigViewModel(
    private val remoteConfig: FirebaseRemoteConfig
) : ViewModel() {

    private val _isPromoEnabled = MutableStateFlow(false)
    val isPromoEnabled: StateFlow<Boolean> = _isPromoEnabled

    init {
        setupRemoteConfig()
        fetchAndActivateConfig()
    }

    private fun setupRemoteConfig() {
        // 1. Establecer valores por defecto (se usarán si la app no se conecta)
        remoteConfig.setDefaultsAsync(
            mapOf(
                "main_promo_enabled" to false,
                "welcome_message" to "Bienvenido por defecto"
            )
        )

        // 2. Configurar el intervalo de búsqueda (para desarrollo se puede reducir)
        val configSettings = remoteConfigSettings {
            // Intervalo mínimo de 0 para forzar la búsqueda en desarrollo.
            // En producción, debe ser > 3600 (1 hora).
            minimumFetchIntervalInSeconds = if (BuildConfig.DEBUG) 0 else 3600
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
    }

    // Función para obtener y activar la configuración
    fun fetchAndActivateConfig() {
        viewModelScope.launch {
            remoteConfig.fetchAndActivate()
                .addOnSuccessListener { updated ->
                    if (updated) {
                        Log.d("RemoteConfig", "Configuración actualizada. Aplicando nuevos valores.")
                    } else {
                        Log.d("RemoteConfig", "Configuración no actualizada. Usando valores en caché.")
                    }

                    // 3. Obtener el valor actualizado (o por defecto)
                    _isPromoEnabled.value = remoteConfig.getBoolean("main_promo_enabled")
                }
                .addOnFailureListener {
                    Log.e("RemoteConfig", "Error al obtener la configuración", it)
                    // En caso de error, el valor por defecto sigue cargado
                    _isPromoEnabled.value = remoteConfig.getBoolean("main_promo_enabled")
                }
        }
    }
}