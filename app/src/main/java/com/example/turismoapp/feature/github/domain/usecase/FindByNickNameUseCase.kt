package com.example.turismoapp.feature.github.domain.usecase


import com.example.turismoapp.feature.github.domain.model.UserModel
import com.example.turismoapp.feature.github.domain.repository.IGithubRepository
import kotlinx.coroutines.delay

class FindByNickNameUseCase(
    val repository: IGithubRepository
) {
    suspend fun invoke(nickname: String) : Result<UserModel>  {
        delay(2000)
        return repository.findByNick(nickname)
    }
}