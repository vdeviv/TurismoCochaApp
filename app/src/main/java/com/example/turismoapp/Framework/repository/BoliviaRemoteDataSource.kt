package com.example.turismoapp.Framework.repository

import android.util.Log
import com.example.turismoapp.Framework.dto.PlaceDto
import com.example.turismoapp.Framework.service.IBoliviaService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BoliviaRemoteDataSource(private val api: IBoliviaService) {

    suspend fun getPlacesByDepartment(dept: String = "Cochabamba"): List<PlaceDto> =
        withContext(Dispatchers.IO) {
            try {
                val res = api.getPlacesByDepartment(dept)
                if (res.isSuccessful) {
                    res.body()?.data ?: emptyList()
                } else {
                    Log.e("BoliviaRemoteDS", "HTTP ${res.code()} - ${res.errorBody()?.string()}")
                    emptyList()
                }
            } catch (e: Exception) {
                Log.e("BoliviaRemoteDS", "Exception: ${e.message}")
                emptyList()
            }
        }
}
