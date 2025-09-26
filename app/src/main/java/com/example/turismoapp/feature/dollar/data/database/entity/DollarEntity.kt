package com.example.turismoapp.feature.dollar.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "dollars")
data class DollarEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,


    @ColumnInfo(name = "dollar_official")
    var dolarOficial: String? = null,


    @ColumnInfo(name = "dollar_parallel")
    var dolarParalelo: String? = null,


    @ColumnInfo(name = "timestamp")
    var timestamp: Long = System.currentTimeMillis())