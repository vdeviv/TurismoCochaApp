package com.example.turismoapp.di

import com.example.turismoapp.R
import com.example.turismoapp.feature.dollar.data.database.AppRoomDatabase
import com.example.turismoapp.feature.dollar.data.datasource.DollarLocalDataSource
import com.example.turismoapp.feature.dollar.data.repository.DollarRepository
import com.example.turismoapp.feature.dollar.data.datasource.RealTimeRemoteDataSource
import com.example.turismoapp.feature.dollar.domain.repository.IDollarRepository
import com.example.turismoapp.feature.dollar.domain.usecase.FetchDollarUseCase
import com.example.turismoapp.feature.dollar.presentation.DollarViewModel
import com.example.turismoapp.feature.github.data.api.GithubService
import com.example.turismoapp.feature.github.data.datasource.GithubRemoteDataSource
import com.example.turismoapp.feature.github.data.repository.GithubRepository
import com.example.turismoapp.feature.github.domain.repository.IGithubRepository
import com.example.turismoapp.feature.github.domain.usecase.FindByNickNameUseCase
import com.example.turismoapp.feature.github.presentation.GithubViewModel
import com.example.turismoapp.feature.movie.data.api.MovieService
import com.example.turismoapp.feature.movie.data.datasource.MovieRemoteDataSource
import com.example.turismoapp.feature.movie.data.repository.MovieRepository
import com.example.turismoapp.feature.movie.domain.repository.IMoviesRepository
import com.example.turismoapp.feature.movie.domain.usecase.FetchPopularMoviesUseCase
import com.example.turismoapp.feature.movie.presentation.PopularMoviesViewModel
import com.example.turismoapp.feature.profile.presentation.ProfileViewModel
import com.example.turismoapp.feature.profile.data.repository.ProfileRepository
import com.example.turismoapp.feature.profile.domain.repository.IProfileRepository
import com.example.turismoapp.feature.profile.domain.usecase.GetProfileUseCase
import okhttp3.OkHttpClient
import org.koin.android.BuildConfig
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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

    // Retrofit
    single(named(com.example.turismoapp.di.NetworkConstants.RETROFIT_GITHUB)) {
        Retrofit.Builder()
            .baseUrl(com.example.turismoapp.di.NetworkConstants.GITHUB_BASE_URL)
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Retrofit
    single(named(com.example.turismoapp.di.NetworkConstants.RETROFIT_MOVIE)) {
        Retrofit.Builder()
            .baseUrl(com.example.turismoapp.di.NetworkConstants.MOVIE_BASE_URL)
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // GithubService
    single<GithubService> {
        get<Retrofit>( named(com.example.turismoapp.di.NetworkConstants.RETROFIT_GITHUB)).create(GithubService::class.java)
    }
    single{ GithubRemoteDataSource(get()) }
    single<IGithubRepository>{ GithubRepository(get()) }

    factory { FindByNickNameUseCase(get()) }
    viewModel { GithubViewModel(get(), get()) }

    single<IProfileRepository> { ProfileRepository() }
    factory { GetProfileUseCase(get()) }
    viewModel { ProfileViewModel(get()) }

    single { AppRoomDatabase.getDatabase(get()) }
    single { get<AppRoomDatabase>().dollarDao() }
    single { RealTimeRemoteDataSource() }
    single { DollarLocalDataSource(get()) }
    single<IDollarRepository> { DollarRepository(get(), get()) }
    factory { FetchDollarUseCase(get()) }
    viewModel{ DollarViewModel(get()) }


    single(named("apiKey")) {
        androidApplication().getString(R.string.api_key)
    }

    single<MovieService> {
        get<Retrofit>(named(com.example.turismoapp.di.NetworkConstants.RETROFIT_MOVIE)).create(MovieService::class.java)
    }
    single { MovieRemoteDataSource(get(), get(named("apiKey"))) }
    single<IMoviesRepository> { MovieRepository(get()) }
    factory { FetchPopularMoviesUseCase(get()) }
    viewModel{ PopularMoviesViewModel(get()) }

}

