package com.turismoapp.mayuandino.feature.packages.presentation

import com.turismoapp.mayuandino.feature.packages.domain.model.PackageModel

data class PackageUiState(
    val isLoading: Boolean = true,
    val packages: List<PackageModel> = emptyList(),
    val error: String? = null
)
