package com.turismoapp.mayuandino.utils

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging

fun saveFcmTokenToFirestore() {
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    val userId = auth.currentUser?.uid
    if (userId == null) {
        Log.w("FCM", "No se puede guardar el token: Usuario no autenticado.")
        return
    }

    FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
        if (!task.isSuccessful) {
            Log.e("FCM", "Fallo al obtener token después de Auth.", task.exception)
            return@addOnCompleteListener
        }
        val token = task.result

        val tokenData = hashMapOf(
            "token" to token,
            "timestamp" to System.currentTimeMillis()
        )

        db.collection("fcmTokens")
            .document(userId)
            .set(tokenData)
            .addOnSuccessListener {
                Log.d("FCM", "✅ Token guardado EXPLICITAMENTE después del Login/Register.")
            }
            .addOnFailureListener { e ->
                Log.e("FCM", "❌ Error al guardar token explícitamente.", e)
            }
    }
}