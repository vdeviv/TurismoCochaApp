package com.example.turismoapp.feature.login.data

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task

class GoogleSignInHelper(private val context: Context) {

    private val googleSignInClient: GoogleSignInClient

    init {
        // IMPORTANTE: Reemplaza este string con tu Web Client ID de Firebase
        // Lo encuentras en Firebase Console > Project Settings > General > Web API Key
        // O en tu archivo google-services.json busca "oauth_client" con "client_type": 3
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("TU_WEB_CLIENT_ID_AQUI") // ← CAMBIAR ESTO
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(context, gso)
    }

    fun getSignInIntent(): Intent {
        return googleSignInClient.signInIntent
    }

    fun getSignedInAccountFromIntent(data: Intent?): Task<GoogleSignInAccount> {
        return GoogleSignIn.getSignedInAccountFromIntent(data)
    }

    fun signOut() {
        googleSignInClient.signOut()
    }

    companion object {
        const val RC_SIGN_IN = 9001
    }
}