package com.example.turismoapp.Framework.service

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitBuilder(private val context: Context) {

    private fun getRetrofit(baseUrl: String): Retrofit {
        val logger = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val client = OkHttpClient.Builder()
            .addInterceptor(logger)
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
        private const val BASE_URL_BOLIVIA = "https://bolivia-api.com"
    }
}
