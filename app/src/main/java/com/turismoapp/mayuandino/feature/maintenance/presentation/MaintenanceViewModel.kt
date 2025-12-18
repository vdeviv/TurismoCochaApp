package com.turismoapp.mayuandino.feature.maintenance.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.turismoapp.mayuandino.feature.maintenance.data.MaintenanceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MaintenanceViewModel(
    private val repository: MaintenanceRepository
) : ViewModel() {

    private val _maintenanceMode = MutableStateFlow(false)
    val maintenanceMode: StateFlow<Boolean> = _maintenanceMode

    init {
        viewModelScope.launch {
            repository.observeMaintenanceStatus().collect {
                _maintenanceMode.value = it
            }
        }

        viewModelScope.launch {
            repository.fetchMaintenanceStatus().collect {
                _maintenanceMode.value = it
            }
        }
    }
}
