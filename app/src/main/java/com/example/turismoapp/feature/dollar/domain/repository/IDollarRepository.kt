package com.example.turismoapp.feature.dollar.domain.repository

import com.example.turismoapp.feature.dollar.domain.model.DollarModel
import kotlinx.coroutines.flow.Flow

interface IDollarRepository {
   suspend fun getDollar(): Flow<DollarModel>
}