package com.example.turismoapp.feature.login.domain.usecase

import com.example.turismoapp.feature.login.domain.model.AuthUser
import com.example.turismoapp.feature.login.domain.model.Result
import com.example.turismoapp.feature.login.domain.repository.IAuthRepository
import com.google.android.gms.auth.api.signin.GoogleSignInAccount

class SignInWithGoogleUseCase(
    private val repository: IAuthRepository
)