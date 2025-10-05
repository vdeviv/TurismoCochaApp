package com.example.turismoapp.features.onboarding.presentation
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.turismoapp.features.onboarding.domain.usecase.IsOnboardingCompletedUseCase
import com.example.turismoapp.features.onboarding.domain.usecase.SaveOnboardingCompletedUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class OnboardingViewModel(
    private val isOnboardingCompletedUseCase: IsOnboardingCompletedUseCase,
    private val saveOnboardingCompletedUseCase: SaveOnboardingCompletedUseCase
) : ViewModel() {

    val isOnboardingCompleted = isOnboardingCompletedUseCase()
        .stateIn(viewModelScope, SharingStarted.Lazily, false)

    fun completeOnboarding() {
        viewModelScope.launch {
            saveOnboardingCompletedUseCase(true)
        }
    }
}
