package com.turismoapp.mayuandino.feature.profile.domain.repository

import com.turismoapp.mayuandino.feature.profile.domain.model.ProfileModel

interface IProfileRepository {
    suspend fun fetchProfile(uid: String): Result<ProfileModel>
    suspend fun saveProfile(profile: ProfileModel): Result<Unit>
    suspend fun updateProfile(profile: ProfileModel): Result<Unit>
    suspend fun deleteProfile(uid: String): Result<Unit>
}