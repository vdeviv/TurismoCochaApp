package com.turismoapp.mayuandino.feature.packages.domain.usecase

import com.turismoapp.mayuandino.feature.packages.domain.repository.IPackagesRepository

class GetPackageUseCase(
    private val repository: IPackagesRepository
) {
    suspend operator fun invoke() = repository.getPackages()
}
