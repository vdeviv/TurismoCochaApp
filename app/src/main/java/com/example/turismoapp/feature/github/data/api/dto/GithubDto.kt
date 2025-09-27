package com.example.turismoapp.feature.github.data.api.dto

import com.google.gson.annotations.SerializedName


data class GithubDto(val login: String,
                     @SerializedName("avatar_url") val url: String)
