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

// 🔥 FIREBASE IMPORTS
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
// 🔥 AUTH
import com.example.turismoapp.feature.login.data.repository.AuthRepository
import com.example.turismoapp.feature.login.domain.repository.IAuthRepository

// 🔥 PROFILE
import com.example.turismoapp.feature.profile.presentation.ProfileViewModel
import com.example.turismoapp.feature.profile.data.repository.ProfileRepository // Importar la implementación correcta
import com.example.turismoapp.feature.profile.domain.repository.IProfileRepository
import com.example.turismoapp.feature.profile.domain.usecase.GetProfileUseCase
import com.example.turismoapp.feature.profile.domain.usecase.SaveProfileUseCase
import com.example.turismoapp.feature.profile.domain.usecase.UpdateProfileUseCase
import com.example.turismoapp.feature.profile.domain.usecase.DeleteProfileUseCase
import com.example.turismoapp.feature.profile.domain.usecase.UploadAvatarUseCase

// ✅ IMPORTAR LAS INTERFACES DE STORAGE
import com.example.turismoapp.feature.profile.domain.repository.IStorageRepository
import com.example.turismoapp.feature.profile.data.repository.StorageRepositoryImpl

// 🟢 ONBOARDING
import com.example.turismoapp.feature.onboarding.data.datastore.OnboardingDataStore
import com.example.turismoapp.feature.onboarding.data.repository.OnboardingRepository
import com.example.turismoapp.feature.onboarding.domain.repository.IOnboardingRepository
import com.example.turismoapp.feature.onboarding.domain.usecase.IsOnboardingCompletedUseCase
import com.example.turismoapp.feature.onboarding.domain.usecase.SaveOnboardingCompletedUseCase
import com.example.turismoapp.feature.onboarding.presentation.OnboardingViewModel

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

    // --- Retrofit: GitHub
    single(named(NetworkConstants.RETROFIT_GITHUB)) {
        Retrofit.Builder()
            .baseUrl(NetworkConstants.GITHUB_BASE_URL)
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // --- Retrofit: Movies
    single(named(NetworkConstants.RETROFIT_MOVIE)) {
        Retrofit.Builder()
            .baseUrl(NetworkConstants.MOVIE_BASE_URL)
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // 🔥 FIREBASE - NUEVAS INSTANCIAS (Añadir Storage)
    single { FirebaseAuth.getInstance() }
    single { FirebaseFirestore.getInstance() }
    // ✅ 1. Definir la instancia de Firebase Storage
    single { FirebaseStorage.getInstance() }


    // 🔥 AUTH REPOSITORY
    single<IAuthRepository> { AuthRepository(get()) } // Usa FirebaseAuth


    // --- GitHub Module ---
    single<GithubService> {
        get<Retrofit>(named(NetworkConstants.RETROFIT_GITHUB))
            .create(GithubService::class.java)
    }
    single { GithubRemoteDataSource(get()) }
    single<IGithubRepository> { GithubRepository(get()) }
    factory { FindByNickNameUseCase(get()) }
    viewModel { GithubViewModel(get(), get()) }


    // --- PROFILE MODULE ---

    // Repositorios
    single<IProfileRepository> { ProfileRepository(get()) } // Usa FirebaseFirestore
    single<IStorageRepository> { StorageRepositoryImpl(get()) } // Usa FirebaseStorage

    // Use Cases
    factory { GetProfileUseCase(get()) }
    factory { SaveProfileUseCase(get()) }
    factory { UpdateProfileUseCase(get()) }
    factory { DeleteProfileUseCase(get()) }
    factory { UploadAvatarUseCase(get()) }

    // ViewModel
    viewModel {
        ProfileViewModel(
            getProfileUseCase = get(),
            saveProfileUseCase = get(),
            updateProfileUseCase = get(),
            deleteProfileUseCase = get(),
            uploadAvatarUseCase = get(),
            firebaseAuth = get() // Usa FirebaseAuth
        )
    }
    // --- FIN PROFILE MODULE ---


    // --- Dollar Module ---
    single { AppRoomDatabase.getDatabase(get()) }
    single { get<AppRoomDatabase>().dollarDao() }
    single { RealTimeRemoteDataSource() }
    single { DollarLocalDataSource(get()) }
    single<IDollarRepository> { DollarRepository(get(), get()) }
    factory { FetchDollarUseCase(get()) }
    viewModel { DollarViewModel(get()) }


    // --- Movie Module ---
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


    // --- Onboarding Module ---
    single { OnboardingDataStore(get<android.content.Context>()) }

    single<IOnboardingRepository> {
        OnboardingRepository(get<OnboardingDataStore>())
    }

    factory { IsOnboardingCompletedUseCase(get<IOnboardingRepository>()) }

    factory { SaveOnboardingCompletedUseCase(get<IOnboardingRepository>()) }

    viewModel {
        OnboardingViewModel(
            get<IsOnboardingCompletedUseCase>(),
            get<SaveOnboardingCompletedUseCase>()
        )
    }
}