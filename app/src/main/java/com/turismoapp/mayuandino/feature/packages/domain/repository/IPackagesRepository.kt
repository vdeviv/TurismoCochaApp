package com.turismoapp.mayuandino.feature.packages.domain.repository

import com.turismoapp.mayuandino.feature.packages.domain.model.PackageModel

interface IPackagesRepository {
    suspend fun getPackages(): List<PackageModel>
}
