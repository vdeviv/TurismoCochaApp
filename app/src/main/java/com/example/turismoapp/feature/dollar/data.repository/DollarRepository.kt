package com.example.turismoapp.feature.dollar.data.repository

import com.example.turismoapp.feature.dollar.datasource.RealTimeRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import com.example.turismoapp.feature.dollar.domain.model.DollarModel
import com.example.turismoapp.feature.dollar.domain.repository.IDollarRepository

class  DollarRepository(val realTimeRemoteDataSource: RealTimeRemoteDataSource): IDollarRepository {
    override suspend fun getDollar(): Flow<DollarModel> {
        return flow {
            emit(DollarModel("6.96", "12.6"))
        }
        return realTimeRemoteDataSource.getDollarUpdates()
    }
}