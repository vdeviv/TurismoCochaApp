package com.turismoapp.mayuandino.feature.dollar.domain.repository

import com.turismoapp.mayuandino.feature.dollar.domain.model.DollarModel
import kotlinx.coroutines.flow.Flow

interface IDollarRepository {
   suspend fun getDollar(): Flow<DollarModel>
}