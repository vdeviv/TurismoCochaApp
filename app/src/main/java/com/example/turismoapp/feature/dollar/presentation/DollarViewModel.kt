package com.example.turismoapp.feature.dollar.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.turismoapp.feature.dollar.domain.model.DollarModel
import com.example.turismoapp.feature.dollar.domain.usecase.FetchDollarUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers

class DollarViewModel(
    val fetchDollarUseCase: FetchDollarUseCase
) : ViewModel() {
    sealed class DollarUIState {
        object Loading : DollarUIState()
        class Error(val message: String) : DollarUIState()
        class Success(val data: DollarModel) : DollarUIState()
    }
    init {
        getDollar()
    }

    private val _uiState = MutableStateFlow<DollarUIState>(DollarUIState.Loading)
    val uiState: StateFlow<DollarUIState> = _uiState



    fun getDollar() {
        viewModelScope.launch(Dispatchers.IO) {
            fetchDollarUseCase.invoke().collect{
                data -> _uiState.value = DollarUIState.Success(data)
            }
        }
    }
}
