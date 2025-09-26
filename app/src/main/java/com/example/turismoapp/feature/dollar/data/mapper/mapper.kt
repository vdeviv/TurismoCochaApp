package com.example.turismoapp.feature.dollar.data.mapper


import com.example.turismoapp.feature.dollar.data.database.entity.DollarEntity
import com.example.turismoapp.feature.dollar.domain.model.DollarModel


fun DollarEntity.toModel() : DollarModel {
    return DollarModel(
        dolarOficial = dolarOficial,
        dolarParalelo = dolarParalelo
    )
}


fun DollarModel.toEntity(): DollarEntity {
    return DollarEntity(
        dolarOficial = dolarOficial,
        dolarParalelo = dolarParalelo
    )
}
