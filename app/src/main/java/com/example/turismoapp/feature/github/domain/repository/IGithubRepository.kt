package com.example.turismoapp.feature.github.domain.repository

import com.example.turismoapp.feature.github.domain.model.UserModel

interface IGithubRepository {
    suspend fun findByNick(value: String): Result<UserModel>
}