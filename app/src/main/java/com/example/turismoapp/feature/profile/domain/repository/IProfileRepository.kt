package com.example.turismoapp.feature.profile.domain.repository

import com.example.turismoapp.feature.profile.domain.model.ProfileModel


interface IProfileRepository {
    fun fetchData(): Result<ProfileModel>
}