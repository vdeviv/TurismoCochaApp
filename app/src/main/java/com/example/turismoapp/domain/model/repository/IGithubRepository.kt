package com.example.turismoapp.domain.model.repository

import com.example.turismoapp.domain.model.UserModel

interface IGithubRepository {
    fun findByNick(value: String): Result<UserModel>
}