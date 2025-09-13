package com.example.turismoapp.feature.dollar.data.repository

import com.example.turismoapp.feature.dollar.datasource.DollarLocalDataSource
import com.example.turismoapp.feature.dollar.datasource.RealTimeRemoteDataSource
import kotlinx.coroutines.flow.Flow
import com.example.turismoapp.feature.dollar.domain.model.DollarModel
import com.example.turismoapp.feature.dollar.domain.repository.IDollarRepository

class DollarRepository(
    val realTimeRemoteDataSource: RealTimeRemoteDataSource,
    val localDataSource: DollarLocalDataSource
): IDollarRepository {

    override suspend fun getDollar(): Flow<DollarModel> {
        realTimeRemoteDataSource.getDollarUpdates()
            .collect { dollar ->
                localDataSource.insert(dollar)
            }
        return realTimeRemoteDataSource.getDollarUpdates()
    }
}
