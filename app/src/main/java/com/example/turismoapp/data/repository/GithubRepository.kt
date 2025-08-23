package com.example.turismoapp.data.repository

import com.example.turismoapp.domain.model.UserModel
import com.example.turismoapp.domain.model.repository.IGithubRepository

class GithubRepository: IGithubRepository {
    override fun findByNick(value: String): Result<UserModel> {
        return Result.success( UserModel(
            nickname = "calyr", pathUrl = "https://avatars.githubusercontent.com/u/874321?v=4", summary = "String"))

    }

}