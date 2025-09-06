package com.example.turismoapp.feature.github.domain.model.usecase
import android.R
import com.example.turismoapp.feature.github.domain.model.UserModel
import com.example.turismoapp.feature.github.domain.model.repository.IGithubRepository


class FindByNicknameUseCase
    (    val repository: IGithubRepository
)
{

    fun invoke(nickname: String) :  Result<UserModel>{
        return repository.findByNick(value = nickname)
    }
}