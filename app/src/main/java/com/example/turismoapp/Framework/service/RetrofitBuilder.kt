package com.example.turismoapp.Framework.service

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitBuilder(private val context: Context) {

    private fun getRetrofit(baseUrl: String): Retrofit {
        // Interceptor para ver logs de peticiones y respuestas HTTP
        val logger = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        // Cliente OkHttp con el interceptor
        val client = OkHttpClient.Builder()
            .addInterceptor(logger)
            .build()

        // Construcción del cliente Retrofit con GSON como convertidor JSON
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create()) // ✅ ahora usa GSON
            .client(client)
            .build()
    }

    // Servicio para la Bolivia API
    val boliviaService: IBoliviaService by lazy {
        getRetrofit(BASE_URL_BOLIVIA).create(IBoliviaService::class.java)
    }

    companion object {
        private const val BASE_URL_BOLIVIA = "https://bolivia-api.com/"
    }
}

