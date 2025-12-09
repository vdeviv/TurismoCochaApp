package com.turismoapp.mayuandino.di

import com.turismoapp.mayuandino.R
import com.turismoapp.mayuandino.feature.dollar.data.database.AppRoomDatabase
import com.turismoapp.mayuandino.feature.dollar.data.datasource.DollarLocalDataSource
import com.turismoapp.mayuandino.feature.dollar.data.datasource.RealTimeRemoteDataSource
import com.turismoapp.mayuandino.feature.dollar.data.repository.DollarRepository
import com.turismoapp.mayuandino.feature.dollar.domain.repository.IDollarRepository
import com.turismoapp.mayuandino.feature.dollar.domain.usecase.FetchDollarUseCase
import com.turismoapp.mayuandino.feature.dollar.presentation.DollarViewModel

import com.turismoapp.mayuandino.feature.github.data.api.GithubService
import com.turismoapp.mayuandino.feature.github.data.datasource.GithubRemoteDataSource
import com.turismoapp.mayuandino.feature.github.data.repository.GithubRepository
import com.turismoapp.mayuandino.feature.github.domain.repository.IGithubRepository
import com.turismoapp.mayuandino.feature.github.domain.usecase.FindByNickNameUseCase
import com.turismoapp.mayuandino.feature.github.presentation.GithubViewModel

import com.turismoapp.mayuandino.feature.movie.data.api.MovieService
import com.turismoapp.mayuandino.feature.movie.data.datasource.MovieRemoteDataSource
import com.turismoapp.mayuandino.feature.movie.data.repository.MovieRepository
import com.turismoapp.mayuandino.feature.movie.domain.repository.IMoviesRepository
import com.turismoapp.mayuandino.feature.movie.domain.usecase.FetchPopularMoviesUseCase
import com.turismoapp.mayuandino.feature.movie.presentation.PopularMoviesViewModel

// Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

// Auth
import com.turismoapp.mayuandino.feature.login.data.repository.AuthRepository
import com.turismoapp.mayuandino.feature.login.domain.repository.IAuthRepository

// Profile
import com.turismoapp.mayuandino.feature.profile.presentation.ProfileViewModel
import com.turismoapp.mayuandino.feature.profile.data.repository.ProfileRepository
import com.turismoapp.mayuandino.feature.profile.domain.repository.IProfileRepository
import com.turismoapp.mayuandino.feature.profile.domain.repository.IStorageRepository
import com.turismoapp.mayuandino.feature.profile.data.repository.StorageRepositoryImpl
import com.turismoapp.mayuandino.feature.profile.domain.usecase.*

// Onboarding
import com.turismoapp.mayuandino.feature.onboarding.data.datastore.OnboardingDataStore
import com.turismoapp.mayuandino.feature.onboarding.data.repository.OnboardingRepository
import com.turismoapp.mayuandino.feature.onboarding.domain.repository.IOnboardingRepository
import com.turismoapp.mayuandino.feature.onboarding.domain.usecase.IsOnboardingCompletedUseCase
import com.turismoapp.mayuandino.feature.onboarding.domain.usecase.SaveOnboardingCompletedUseCase
import com.turismoapp.mayuandino.feature.onboarding.presentation.OnboardingViewModel

// Home Service para PLACES
import com.turismoapp.mayuandino.feature.home.data.FirebaseHomeService

// PACKAGES MODULE
import com.turismoapp.mayuandino.feature.packages.data.repository.PackagesRepository
import com.turismoapp.mayuandino.feature.packages.domain.repository.IPackagesRepository
import com.turismoapp.mayuandino.feature.packages.domain.usecase.GetPackageUseCase
import com.turismoapp.mayuandino.feature.packages.presentation.PackagesViewModel

import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidApplication
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

    single {
        OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    // --- Retrofit GitHub ---
    single(named(NetworkConstants.RETROFIT_GITHUB)) {
        Retrofit.Builder()
            .baseUrl(NetworkConstants.GITHUB_BASE_URL)
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // --- Retrofit Movies ---
    single(named(NetworkConstants.RETROFIT_MOVIE)) {
        Retrofit.Builder()
            .baseUrl(NetworkConstants.MOVIE_BASE_URL)
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Firebase
    single { FirebaseAuth.getInstance() }
    single { FirebaseFirestore.getInstance() }
    single { FirebaseStorage.getInstance() }

    // Auth
    single<IAuthRepository> { AuthRepository(get()) }

    // GitHub
    single<GithubService> {
        get<Retrofit>(named(NetworkConstants.RETROFIT_GITHUB))
            .create(GithubService::class.java)
    }
    single { GithubRemoteDataSource(get()) }
    single<IGithubRepository> { GithubRepository(get()) }
    factory { FindByNickNameUseCase(get()) }
    viewModel { GithubViewModel(get(), get()) }

    // Profile
    single<IProfileRepository> { ProfileRepository(get()) }
    single<IStorageRepository> { StorageRepositoryImpl(get()) }

    factory { GetProfileUseCase(get()) }
    factory { SaveProfileUseCase(get()) }
    factory { UpdateProfileUseCase(get()) }
    factory { DeleteProfileUseCase(get()) }
    factory { UploadAvatarUseCase(get()) }

    viewModel {
        ProfileViewModel(
            get(), get(), get(), get(), get(), get()
        )
    }

    // Dollar
    single { AppRoomDatabase.getDatabase(get()) }
    single { get<AppRoomDatabase>().dollarDao() }
    single { RealTimeRemoteDataSource() }
    single { DollarLocalDataSource(get()) }
    single<IDollarRepository> { DollarRepository(get(), get()) }
    factory { FetchDollarUseCase(get()) }
    viewModel { DollarViewModel(get()) }

    // Movies
    single(named("apiKey")) {
        androidApplication().getString(R.string.api_key)
    }
    single<MovieService> {
        get<Retrofit>(named(NetworkConstants.RETROFIT_MOVIE))
            .create(MovieService::class.java)
    }
    single { MovieRemoteDataSource(get(), get(named("apiKey"))) }
    single<IMoviesRepository> { MovieRepository(get()) }
    factory { FetchPopularMoviesUseCase(get()) }
    viewModel { PopularMoviesViewModel(get()) }

    // Onboarding
    single { OnboardingDataStore(get()) }
    single<IOnboardingRepository> { OnboardingRepository(get()) }
    factory { IsOnboardingCompletedUseCase(get()) }
    factory { SaveOnboardingCompletedUseCase(get()) }
    viewModel { OnboardingViewModel(get(), get()) }

    // --- HOME SERVICE (places) ---
    single { FirebaseHomeService() }

    // --- PACKAGES MODULE ---
    single<IPackagesRepository> { PackagesRepository(get()) }
    factory { GetPackageUseCase(get()) }
    viewModel { PackagesViewModel(get()) }
}
