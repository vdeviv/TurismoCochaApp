package com.turismoapp.mayuandino.feature.dollar.data.datasource

import com.turismoapp.mayuandino.feature.dollar.data.database.dao.IDollarDao

import com.turismoapp.mayuandino.feature.dollar.data.mapper.toModel
import com.turismoapp.mayuandino.feature.dollar.data.mapper.toEntity

import com.turismoapp.mayuandino.feature.dollar.domain.model.DollarModel


class DollarLocalDataSource(
    val dao: IDollarDao
) {


    suspend fun getList(): List<DollarModel> {
        return dao.getList().map {
            it.toModel()
        }


    }
    suspend fun deleteAll() {
        dao.deleteAll()
    }
    suspend fun inserTDollars(list: List<DollarModel>) {
        val dollarEntity = list.map { it.toEntity() }
        dao.insertDollars(dollarEntity)
    }


    suspend fun insert(dollar: DollarModel) {
        dao.insert(dollar.toEntity())
    }


}
