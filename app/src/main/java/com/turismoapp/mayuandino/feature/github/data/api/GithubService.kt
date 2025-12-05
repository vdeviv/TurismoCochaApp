package com.turismoapp.mayuandino.feature.github.data.api

import com.turismoapp.mayuandino.feature.github.data.api.dto.GithubDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface GithubService {
    @GET("/users/{githubLogin}")
    suspend fun getInfoAvatar(@Path("githubLogin") githubLogin: String): Response<GithubDto>
}