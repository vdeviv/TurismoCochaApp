package com.turismoapp.mayuandino.feature.dollar.domain.usecase

import com.turismoapp.mayuandino.feature.dollar.domain.model.DollarModel
import com.turismoapp.mayuandino.feature.dollar.domain.repository.IDollarRepository
import kotlinx.coroutines.flow.Flow

class FetchDollarUseCase(
     val repository: IDollarRepository
) {
    suspend fun invoke(): Flow<DollarModel> {
        return repository.getDollar()
    }
}