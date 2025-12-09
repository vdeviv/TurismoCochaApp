package com.turismoapp.mayuandino.feature.packages.data.repository

import com.turismoapp.mayuandino.feature.home.data.FirebaseHomeService
import com.turismoapp.mayuandino.feature.packages.domain.model.PackageModel
import com.turismoapp.mayuandino.feature.packages.domain.repository.IPackagesRepository

class PackagesRepository(
    private val firebaseHomeService: FirebaseHomeService
) : IPackagesRepository {

    override suspend fun getPackages(): List<PackageModel> {

        val places = firebaseHomeService.getHomePlaces()

        return places.map { dto ->
            PackageModel(
                id = dto.id,
                title = dto.name,
                imageUrl = dto.imageUrl,
                rating = dto.rating,
                description = dto.description,
                city = dto.city
            )
        }
    }
}

