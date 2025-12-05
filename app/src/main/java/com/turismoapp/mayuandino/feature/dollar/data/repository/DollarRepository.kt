package com.turismoapp.mayuandino.feature.dollar.data.repository

import com.turismoapp.mayuandino.feature.dollar.data.datasource.DollarLocalDataSource
import com.turismoapp.mayuandino.feature.dollar.data.datasource.RealTimeRemoteDataSource
import kotlinx.coroutines.flow.Flow
import com.turismoapp.mayuandino.feature.dollar.domain.model.DollarModel
import com.turismoapp.mayuandino.feature.dollar.domain.repository.IDollarRepository

import kotlinx.coroutines.flow.onEach


class DollarRepository(
    val realTimeRemoteDataSource: RealTimeRemoteDataSource,
    val localDataSource: DollarLocalDataSource
): IDollarRepository {


    override suspend fun getDollar(): Flow<DollarModel> {
//        return flow {
//            emit(DollarModel("123", "456"))
//        }
        return realTimeRemoteDataSource.getDollarUpdates()
            .onEach {
                localDataSource.insert(it)
            }
    }
}