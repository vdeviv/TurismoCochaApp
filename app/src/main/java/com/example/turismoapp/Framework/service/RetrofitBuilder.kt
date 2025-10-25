package com.example.turismoapp.Framework.service

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitBuilder(private val context: Context) {

    private fun getRetrofit(baseUrl: String): Retrofit {
        val logger = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(logger)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    val boliviaService: IBoliviaService by lazy {
        getRetrofit(BASE_URL_BOLIVIA).create(IBoliviaService::class.java)
    }

    companion object {
        private const val BASE_URL_BOLIVIA = "https://api.opentripmap.com/0.1/"

        // 🔥 Key temporal para PROBAR (consigue la tuya en https://opentripmap.io/product)
        const val OPENTRIPMAP_API_KEY = "5ae2e3f221c38a28845f05b6c39154ec5f19c48c6e2088f0b77cb9fa"
    }
}
