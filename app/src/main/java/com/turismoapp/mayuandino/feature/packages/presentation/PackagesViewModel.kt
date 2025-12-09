package com.turismoapp.mayuandino.feature.packages.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.turismoapp.mayuandino.feature.packages.domain.usecase.GetPackageUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PackagesViewModel(
    private val getPackagesUseCase: GetPackageUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(PackageUiState())
    val state = _state.asStateFlow()

    init {
        loadPackages()
    }

    private fun loadPackages() {
        viewModelScope.launch {
            try {
                val result = getPackagesUseCase()
                _state.value = PackageUiState(
                    isLoading = false,
                    packages = result
                )
            } catch (e: Exception) {
                _state.value = PackageUiState(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }
}
