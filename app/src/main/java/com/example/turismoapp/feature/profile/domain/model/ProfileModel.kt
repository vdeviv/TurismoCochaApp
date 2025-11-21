package com.example.turismoapp.feature.profile.domain.model

import com.example.turismoapp.feature.profile.domain.vo.EmailAddress
import com.example.turismoapp.feature.profile.domain.vo.PersonName
import com.example.turismoapp.feature.profile.domain.vo.PhoneNumber
import com.example.turismoapp.feature.profile.domain.vo.SummaryText
import com.example.turismoapp.feature.profile.domain.vo.UrlPath


data class ProfileModel(
    val uid: String = "",  // UID del usuario de Firebase Auth
    val pathUrl: String = "",
    val name: String = "",
    val email: String = "",
    val cellphone: String = "",
    val summary: String = ""
)
