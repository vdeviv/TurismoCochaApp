package com.example.turismoapp.di

import com.example.turismoapp.feature.dollar.data.database.AppRoomDatabase
import com.example.turismoapp.feature.dollar.datasource.DollarLocalDataSource
import com.example.turismoapp.feature.dollar.data.repository.DollarRepository
import com.example.turismoapp.feature.dollar.datasource.RealTimeRemoteDataSource
import com.example.turismoapp.feature.dollar.domain.repository.IDollarRepository
import com.example.turismoapp.feature.dollar.domain.usecase.FetchDollarUseCase
import com.example.turismoapp.feature.dollar.presentation.DollarViewModel
//import com.example.turismoapp.feature.github.data.api.GithubService
//import com.example.turismoapp.feature.github.datasource.GithubRemoteDataSource
import com.example.turismoapp.feature.github.data.repository.GithubRepository
//import com.example.turismoapp.feature.github.domain.repository.IGithubRepository
//import com.example.turismoapp.feature.github.domain.usecase.FindByNickNameUseCase
//import com.example.turismoapp.feature.github.presentation.GithubViewModel
//import com.example.turismoapp.feature.profile.application.ProfileViewModel
//import com.example.turismoapp.feature.profile.data.repository.ProfileRepository
//import com.example.turismoapp.feature.profile.domain.repository.IProfileRepository
//import com.example.turismoapp.feature.profile.domain.usecase.GetProfileUseCase
import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


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
