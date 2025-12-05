package com.turismoapp.mayuandino.feature.onboarding.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.turismoapp.mayuandino.feature.onboarding.domain.usecase.IsOnboardingCompletedUseCase
import com.turismoapp.mayuandino.feature.onboarding.domain.usecase.SaveOnboardingCompletedUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class OnboardingViewModel(
    private val isOnboardingCompletedUseCase: IsOnboardingCompletedUseCase,
    private val saveOnboardingCompletedUseCase: SaveOnboardingCompletedUseCase
) : ViewModel() {

    private val _isOnboardingCompleted = MutableStateFlow(false)
    val isOnboardingCompleted: StateFlow<Boolean> = _isOnboardingCompleted

    init {
        viewModelScope.launch {
            _isOnboardingCompleted.value = isOnboardingCompletedUseCase()
        }
    }

    fun completeOnboarding() {
        viewModelScope.launch {
            saveOnboardingCompletedUseCase(completed = true)
            _isOnboardingCompleted.value = true
        }
    }
}
