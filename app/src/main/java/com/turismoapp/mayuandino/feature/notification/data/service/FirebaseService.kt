package com.turismoapp.mayuandino.feature.notification.data.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
// ... otras importaciones
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.turismoapp.mayuandino.MainActivity
import com.turismoapp.mayuandino.R
// ⬇️ NUEVAS IMPORTACIONES
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseService: FirebaseMessagingService() {

    // Instancia de Firestore para guardar el token
    private val db = FirebaseFirestore.getInstance()
    // Instancia de Auth para obtener el UID
    private val auth = FirebaseAuth.getInstance()

    // ... (onMessageReceived sigue igual) ...

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM", "Nuevo Token generado. Guardando en servidor.")

        // Llamar a la función para guardar el token
        sendRegistrationToServer(token)
    }

    /**
     * Guarda el token en Firestore usando el UID del usuario autenticado.
     */
    private fun sendRegistrationToServer(token: String) {

        // 1. Obtener el UID del usuario actualmente logueado
        val userId = auth.currentUser?.uid

        // Si el usuario NO está logueado, no guardamos el token (esperamos a que inicie sesión)
        if (userId == null) {
            Log.w("FCM", "Usuario no autenticado. El token NO se guardó en Firestore.")
            return
        }

        // 2. Crear el objeto de datos
        val tokenData = hashMapOf(
            "token" to token,
            "timestamp" to System.currentTimeMillis()
        )

        // 3. Guardar el documento en la colección 'fcmTokens' usando el UID como ID del Documento
        db.collection("fcmTokens")
            .document(userId)       // ⬅️ UID del usuario
            .set(tokenData)
            .addOnSuccessListener {
                Log.d("FCM", "✅ Token guardado/actualizado en Firestore para el UID: $userId")
            }
            .addOnFailureListener { e ->
                Log.e("FCM", "❌ Error al guardar el token en Firestore", e)
            }
    }


    // Función auxiliar para construir y mostrar la notificación visual
    private fun sendNotification(title: String?, messageBody: String?) {
        // Al tocar la notificación, abrimos la MainActivity
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        val channelId = "gamerteca_global_channel"
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            // IMPORTANTE: Asegúrate de tener un icono pequeño (blanco y transparente)
            // Si no tienes uno, usa el de launcher por ahora, pero lo ideal es un icono monocromático.
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title ?: "Gamerteca")
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Configuración necesaria para Android 8.0+ (Oreo)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Avisos Gamerteca", // Nombre visible para el usuario en ajustes
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0, notificationBuilder.build())
    }
}