package com.turismoapp.mayuandino.di

import com.turismoapp.mayuandino.R


import com.turismoapp.mayuandino.feature.github.data.api.GithubService
import com.turismoapp.mayuandino.feature.github.data.datasource.GithubRemoteDataSource
import com.turismoapp.mayuandino.feature.github.data.repository.GithubRepository
import com.turismoapp.mayuandino.feature.github.domain.repository.IGithubRepository
import com.turismoapp.mayuandino.feature.github.domain.usecase.FindByNickNameUseCase
import com.turismoapp.mayuandino.feature.github.presentation.GithubViewModel


// Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
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

// Calendar — LOCAL
import com.turismoapp.mayuandino.feature.calendar.data.repository.CalendarEventRepositoryImpl
import com.turismoapp.mayuandino.feature.calendar.domain.repository.CalendarEventRepository
import com.turismoapp.mayuandino.feature.calendar.domain.usecase.GetEventsByDateUseCase
import com.turismoapp.mayuandino.feature.calendar.domain.usecase.GetEventsByMonthUseCase
import com.turismoapp.mayuandino.feature.calendar.domain.usecase.InsertCalendarEventUseCase
import com.turismoapp.mayuandino.feature.calendar.presentation.CalendarViewModel

// Calendar — FIREBASE SYNC
import com.turismoapp.mayuandino.feature.calendar.data.repository.CalendarRepository
import com.turismoapp.mayuandino.feature.config.ConfigViewModel

// Places (Home)
import com.turismoapp.mayuandino.feature.home.data.FirebaseHomeService

// Packages
import com.turismoapp.mayuandino.feature.packages.data.repository.PackagesRepository
import com.turismoapp.mayuandino.feature.packages.domain.repository.IPackagesRepository
import com.turismoapp.mayuandino.feature.packages.domain.usecase.GetPackageUseCase
import com.turismoapp.mayuandino.feature.packages.presentation.PackagesViewModel

// Room Database Principal
import com.turismoapp.mayuandino.framework.local.db.AppDatabase

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

    // --------------------------------------------------
    // HTTP CLIENT
    // --------------------------------------------------
    single {
        OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    // --------------------------------------------------
    // FIREBASE
    // --------------------------------------------------
    single { FirebaseAuth.getInstance() }
    single { FirebaseFirestore.getInstance() }
    single { FirebaseStorage.getInstance() }

    // --------------------------------------------------
    // AUTH
    // --------------------------------------------------
    single<IAuthRepository> { AuthRepository(get()) }

    // --------------------------------------------------
    // PROFILE
    // --------------------------------------------------
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

    // --------------------------------------------------
    // CALENDAR DATABASE (Room del Calendario)
    // --------------------------------------------------
    single { AppDatabase.getInstance(get()) }
    single { get<AppDatabase>().calendarEventDao() }

    // Calendar local repository
    single<CalendarEventRepository> { CalendarEventRepositoryImpl(get()) }

    // Calendar Firebase sync
    single { CalendarRepository(firestore = get(), dao = get()) }

    // UseCases
    factory { GetEventsByDateUseCase(get()) }
    factory { GetEventsByMonthUseCase(get()) }
    factory { InsertCalendarEventUseCase(get()) }

    viewModel {
        CalendarViewModel(
            getEventsByDate = get(),
            getEventsByMonth = get(),
            insertEventUseCase = get()
        )
    }


    // --------------------------------------------------
    // HOME PLACES
    // --------------------------------------------------
    single { FirebaseHomeService() }

    // --------------------------------------------------
    // PACKAGES
    // --------------------------------------------------
    single<IPackagesRepository> { PackagesRepository(get()) }
    factory { GetPackageUseCase(get()) }
    viewModel { PackagesViewModel(get()) }



    // --- REMOTE CONFIG ---
    single { FirebaseRemoteConfig.getInstance() }
    viewModel { ConfigViewModel(get()) }

}

