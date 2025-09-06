package com.example.turismoapp.feature.github.domain.model.repository

import com.example.turismoapp.feature.github.domain.model.UserModel

interface IGithubRepository {
    fun findByNick(value: String): Result<UserModel>
}