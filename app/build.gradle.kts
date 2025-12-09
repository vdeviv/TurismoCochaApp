import java.util.Properties

val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localProperties.load(localPropertiesFile.inputStream())
}

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.turismoapp.mayuandino"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.turismoapp.mayuandino"
        minSdk = 26
        targetSdk = 36
        versionCode = 2
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        manifestPlaceholders["MAPS_API_KEY"] = localProperties.getProperty("MAPS_API_KEY") ?: ""
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }
}

dependencies {

    // --- CORE ANDROIDX ---
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx) // Usa la versión de libs.versions.toml (2.9.3)
    implementation(libs.androidx.activity.compose)      // Usa la versión de libs.versions.toml (1.9.0)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    // --- MATERIAL ICONS / FONTAWESOME ---
    implementation("androidx.compose.material:material-icons-extended")
    implementation("br.com.devsrsouza.compose.icons:font-awesome:1.1.0")

    // --- LAYOUT / FOUNDATION ---
    implementation(libs.androidx.foundation.layout.android)

    // --- FIREBASE ---
    implementation(platform(libs.firebase.bom))
    implementation(libs.bundles.firebase)
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.firebase.storage.ktx)
    implementation(libs.firebase.messaging)


    // --- GOOGLE SIGN-IN ---
    implementation("com.google.android.gms:play-services-auth:21.2.0")
    implementation ("com.google.maps.android:maps-compose:2.11.4")
    implementation ("com.google.android.gms:play-services-maps:18.1.0")


    // --- RETROFIT (HTTP CLIENT) ---
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // --- COIL (IMÁGENES EN COMPOSE) ---
    implementation("io.coil-kt:coil-compose:2.6.0")

    // --- LIFECYCLE / VIEWMODEL COMPOSE (SOLO DEJAMOS LAS VERSIONES CORRECTAS Y CONSISTENTES) ---
    implementation(libs.androidx.lifecycle.viewmodel.compose) // Usará 2.5.1 de la sección [versions]

    // --- NAVIGATION COMPOSE ---
    implementation(libs.androidx.navigation.compose)
    implementation(libs.compose.navigation)

    // --- KOIN (INYECCIÓN DE DEPENDENCIAS) ---
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.navigation)
    implementation(libs.koin.androidx.compose)

    // --- ROOM (BASE DE DATOS LOCAL) ---
    implementation(libs.bundles.local)
    implementation(libs.firebase.firestore.ktx)
    implementation("com.google.firebase:firebase-storage-ktx")
    ksp(libs.room.compiler)
    testImplementation(libs.room.testing)

    // --- ACCOMPANIST (PAGINAS Y SCROLL) ---
    implementation(libs.accompanist.pager)
    implementation(libs.accompanist.pager.indicators)

    // --- DATASTORE (PREFERENCIAS) ---
    implementation(libs.androidx.datastore.preferences)

    // --- TESTS ---
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}