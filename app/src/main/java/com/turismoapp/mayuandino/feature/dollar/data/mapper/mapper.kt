package com.turismoapp.mayuandino.feature.dollar.data.mapper


import com.turismoapp.mayuandino.feature.dollar.data.database.entity.DollarEntity
import com.turismoapp.mayuandino.feature.dollar.domain.model.DollarModel


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
