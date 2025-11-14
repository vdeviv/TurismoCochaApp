plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.example.turismoapp"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.turismoapp"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        kotlinCompilerExtensionVersion = "1.5.4"
    }
}

dependencies {

    // --- CORE ANDROIDX ---
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
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
    // ⭐️ Agregado: BOM de Firebase para gestionar todas las versiones de Firebase.
    implementation(platform(libs.firebase.bom))

    implementation(libs.firebase.database)
    implementation(libs.firebase.messaging)

    // ⭐️ Agregado: Firebase Authentication (Auth)
    implementation(libs.firebase.auth.ktx)

    // ⭐️ Agregado: Coroutines para poder usar .await() en las tareas de Firebase
    implementation(libs.kotlinx.coroutines.play.services)

    // --- RETROFIT (HTTP CLIENT) ---
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0") // ✅ usamos GSON
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // --- COIL (IMÁGENES EN COMPOSE) ---
    implementation("io.coil-kt:coil-compose:2.6.0")

    // --- LIFECYCLE / VIEWMODEL COMPOSE ---
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.6")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.6")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.6")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.6")

    // --- NAVIGATION COMPOSE ---
    implementation(libs.androidx.navigation.compose)
    implementation(libs.compose.navigation)

    // --- KOIN (INYECCIÓN DE DEPENDENCIAS) ---
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.navigation)
    implementation(libs.koin.androidx.compose)

    // --- ROOM (BASE DE DATOS LOCAL) ---
    implementation(libs.bundles.local)
    // Nota: 'annotationProcessor' ya no se usa con KSP. Mantienes la línea de 'ksp' que es la correcta.
    // annotationProcessor(libs.room.compiler)
    ksp(libs.room.compiler)
    testImplementation(libs.room.testing)

    // --- ACCOMPANIST (PAGINAS Y SCROLL) ---
    implementation(libs.accompanist.pager)
    implementation(libs.accompanist.pager.indicators)

    // --- DATASTORE (PREFERENCIAS) ---
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.firebase.ktx)

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