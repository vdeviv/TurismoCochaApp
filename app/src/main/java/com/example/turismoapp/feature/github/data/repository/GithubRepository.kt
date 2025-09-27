package com.example.turismoapp.feature.github.data.repository


import com.example.turismoapp.feature.github.data.api.GithubService
import com.example.turismoapp.feature.github.data.api.dto.GithubDto
import com.example.turismoapp.feature.github.data.error.DataException


import com.example.turismoapp.feature.github.data.datasource.GithubRemoteDataSource
import com.example.turismoapp.feature.github.domain.error.Failure
import com.example.turismoapp.feature.github.domain.model.UserModel
import com.example.turismoapp.feature.github.domain.repository.IGithubRepository

class GithubRepository(
    val remoteDataSource: GithubRemoteDataSource
): IGithubRepository {
    override suspend fun findByNick(value: String): Result<UserModel> {
        if(value.isEmpty()) {
            return Result.failure(Exception("El campo no puede estar vacio"))
        }
        val response = remoteDataSource.getUser(value)

        response.fold(
            onSuccess = {
                    it ->
                return Result.success(UserModel(
                    nickname = it.login,
                    pathUrl = it.url
                ))
            },
            onFailure = { exception ->
                val failure = when (exception) {
                    is DataException.Network -> Failure.NetworkConnection
                    is DataException.HttpNotFound -> Failure.NotFound
                    is DataException.NoContent -> Failure.EmptyBody
                    is DataException.Unknown -> Failure.Unknown(exception)
                    else -> Failure.Unknown(exception)
                }
                return Result.failure(failure)
            }
        )
    }
}