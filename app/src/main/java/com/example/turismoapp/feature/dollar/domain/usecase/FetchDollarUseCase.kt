package com.example.turismoapp.feature.dollar.domain.usecase

import com.example.turismoapp.feature.dollar.domain.model.DollarModel
import com.example.turismoapp.feature.dollar.domain.repository.IDollarRepository
import kotlinx.coroutines.flow.Flow

class FetchDollarUseCase(
    private val repository: IDollarRepository
) {
    suspend fun invoke(): Flow<DollarModel> {
        return repository.getDollar()
    }
}