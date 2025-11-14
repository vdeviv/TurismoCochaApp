package com.example.turismoapp.feature.profile.data.repository

import com.example.turismoapp.feature.profile.domain.model.ProfileModel
import com.example.turismoapp.feature.profile.domain.repository.IProfileRepository

class ProfileRepository : IProfileRepository {

    override fun fetchData(): Result<ProfileModel> {
        return Result.success(
            ProfileModel(
                name = "Vivi Revollo",
                email = "vivi.omg@gmail.com",
                cellphone = "+777 7777",
                pathUrl = "https://cdn-icons-png.flaticon.com/512/149/149071.png", // URL VÁLIDA
                summary = "No me está dando el ViewModel help"
            )
        )
    }
}
