package com.turismoapp.mayuandino.feature.github.domain.repository

import com.turismoapp.mayuandino.feature.github.domain.model.UserModel

interface IGithubRepository {
    suspend fun findByNick(value: String): Result<UserModel>
}