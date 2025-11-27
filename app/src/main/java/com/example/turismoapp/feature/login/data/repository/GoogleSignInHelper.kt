package com.example.turismoapp.feature.login.data.repository

import android.content.Context
import com.example.turismoapp.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions


class GoogleSignInHelper(val context: Context) {

    private val gso: GoogleSignInOptions by lazy {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            // Usar el string resource para el Web Client ID
            .requestIdToken(context.getString(R.string.web_client_id))
            .requestEmail()
            .build()
    }

    val googleSignInClient: GoogleSignInClient by lazy {
        // La clase GoogleSignIn no está realmente obsoleta para este propósito,
        // pero debes asegurarte de que uses las dependencias correctas en tu build.gradle.
        GoogleSignIn.getClient(context, gso)
    }
}