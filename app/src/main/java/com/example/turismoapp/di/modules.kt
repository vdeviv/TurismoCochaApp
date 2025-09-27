package com.example.turismoapp.di

import com.example.turismoapp.feature.dollar.data.database.AppRoomDatabase
import com.example.turismoapp.feature.dollar.data.datasource.DollarLocalDataSource
import com.example.turismoapp.feature.dollar.data.repository.DollarRepository
import com.example.turismoapp.feature.dollar.data.datasource.RealTimeRemoteDataSource
import com.example.turismoapp.feature.dollar.domain.repository.IDollarRepository
import com.example.turismoapp.feature.dollar.domain.usecase.FetchDollarUseCase
import com.example.turismoapp.feature.dollar.presentation.DollarViewModel
import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

import java.util.concurrent.TimeUnit


object NetworkConstants {
    const val RETROFIT_GITHUB = "RetrofitGithub"
    const val GITHUB_BASE_URL = "https://api.github.com/"
    const val RETROFIT_MOVIE = "RetrofitMovie"
    const val MOVIE_BASE_URL = "https://api.themoviedb.org/"
}

val appModule = module {
    // OkHttpClient
    single {
        OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    single { AppRoomDatabase.getDatabase(get()) }
    single { get<AppRoomDatabase>().dollarDao() }
    single { RealTimeRemoteDataSource() }
    single { DollarLocalDataSource(get()) }
    single<IDollarRepository> { DollarRepository(get(), get()) }
    factory { FetchDollarUseCase(get()) }
    viewModel{ DollarViewModel(get()) }

}

