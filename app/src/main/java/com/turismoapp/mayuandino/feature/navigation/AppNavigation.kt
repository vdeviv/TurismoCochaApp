package com.turismoapp.mayuandino.feature.navigation

// Compose
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// Navigation
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

// Lifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

// UI Screens
import com.turismoapp.mayuandino.feature.splash.presentation.SplashScreen
import com.turismoapp.mayuandino.feature.onboarding.presentation.OnboardingScreen
import com.turismoapp.mayuandino.feature.login.presentation.LoginScreen
import com.turismoapp.mayuandino.feature.login.presentation.RegisterScreen
import com.turismoapp.mayuandino.feature.home.presentation.HomeScreen
import com.turismoapp.mayuandino.feature.movie.presentation.PopularMoviesScreen
import com.turismoapp.mayuandino.feature.profile.presentation.EditProfileScreen
import com.turismoapp.mayuandino.feature.profile.presentation.ProfileScreen
import com.turismoapp.mayuandino.feature.search.presentation.SearchViewModel
import com.turismoapp.mayuandino.feature.search.presentation.PlaceDetailScreen

// Calendar
import com.turismoapp.mayuandino.feature.calendar.presentation.CalendarScreen
import com.turismoapp.mayuandino.feature.calendar.presentation.CalendarViewModel

// Koin
import org.koin.androidx.compose.koinViewModel

import com.turismoapp.mayuandino.feature.splash.presentation.SplashScreen
// ...
import com.turismoapp.mayuandino.feature.search.presentation.PlaceDetailScreen

// 🎯 NUEVAS IMPORTACIONES DE NOTIFICACIONES 🎯
import com.turismoapp.mayuandino.feature.notification.presentation.NotificationViewModel
import com.turismoapp.mayuandino.feature.notification.presentation.NotificationScreen
import com.turismoapp.mayuandino.feature.notification.presentation.NotificationViewModelFactory


import android.app.Application
import androidx.compose.ui.platform.LocalContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    // SEARCH VIEWMODEL
    val searchVM: SearchViewModel = viewModel()
    val allPlaces by searchVM.allPlaces.collectAsState()

    var isSearchOpen by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val filteredPlaces = allPlaces.filter {
        it.name.contains(searchText, ignoreCase = true) ||
                it.description.contains(searchText, ignoreCase = true) ||
                it.city.contains(searchText, ignoreCase = true)
    }

    // RUTAS SIN BOTTOM BAR
    val noBottomRoutes = setOf(
        Screen.Splash.route,
        Screen.Onboarding.route,
        Screen.Login.route,
        Screen.Register.route
    )

    val backStackEntry by navController.currentBackStackEntryAsState()
    val showBottomBar = backStackEntry?.destination?.route !in noBottomRoutes


    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomBar(
                    navController = navController,
                    onSearchClick = { isSearchOpen = true }
                )
            }
        }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = Screen.Splash.route,
            modifier = Modifier.padding(innerPadding)
        ) {

            // ---------------- SPLASH ----------------
            composable(Screen.Splash.route) {
                SplashScreen(
                    onNavigateNext = {
                        navController.navigate(Screen.Onboarding.route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    }
                )
            }

            // ---------------- ONBOARDING ----------------
            composable(Screen.Onboarding.route) {
                OnboardingScreen(
                    onSkip = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Onboarding.route) { inclusive = true }
                        }
                    },
                    onFinish = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Onboarding.route) { inclusive = true }
                        }
                    }
                )
            }

            // ---------------- LOGIN ----------------
            composable(Screen.Login.route) {
                LoginScreen(
                    onSuccess = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    },
                    onRegisterClick = { navController.navigate(Screen.Register.route) }
                )
            }

            // ---------------- REGISTER ----------------
            composable(Screen.Register.route) {
                RegisterScreen(
                    onRegistrationSuccess = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Register.route) { inclusive = true }
                        }
                    },
                    onBackToLogin = {
                        navController.navigate(Screen.Login.route)
                    }
                )
            }

            // ---------------- HOME (NUEVO) ----------------
            composable(Screen.Home.route) {
                HomeScreen(
                    onProfileClick = { navController.navigate(Screen.Profile.route) },
                    onNotificationClick = { navController.navigate(Screen.Notifications.route) },
                    onPlaceClick = { id ->
                        navController.navigate(Screen.DetailPlace.create(id))
                    }
                )
            }

            composable(Screen.Notifications.route) { // Ruta: "notification"
                val application = LocalContext.current.applicationContext as Application

                // Instanciamos el ViewModel
                val viewModel: NotificationViewModel = viewModel(
                    factory = NotificationViewModelFactory(application)
                )

                // ⬇️ LLAMAR AL COMPONENTE DE LA PANTALLA ⬇️
                NotificationScreen(
                    viewModel = viewModel,
                    onBackClick = {
                        navController.popBackStack() // Para volver a la pantalla anterior
                    }
                )
            }

            // ---------------- CALENDAR ----------------
            composable(Screen.Calendar.route) {
                val calendarVM: CalendarViewModel = koinViewModel()
                CalendarScreen(
                    navController = navController,
                    viewModel = calendarVM
                )
            }

            // ---------------- MOVIES ----------------
            composable(Screen.PopularMovies.route) {
                PopularMoviesScreen()
            }

            // ---------------- PROFILE ----------------
            composable(Screen.Profile.route) {
                ProfileScreen(
                    onEditProfileClick = { navController.navigate(Screen.EditProfile.route) },
                    onSignOut = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    },
                    onDeleteAccount = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    }
                )
            }

            // ---------------- EDIT PROFILE ----------------
            composable(Screen.EditProfile.route) {
                EditProfileScreen(
                    onBack = { navController.popBackStack() }
                )
            }

            // RUTAS SIMPLES
            composable(Screen.Favorites.route) { Text("Favoritos") }
            composable(Screen.Trips.route) { Text("Mis Viajes") }
            composable(Screen.Settings.route) { Text("Configuración") }
            composable(Screen.Language.route) { Text("Idioma") }

            // ---------------- DETAIL PLACE ----------------
            composable(
                route = Screen.DetailPlace.route,
                arguments = listOf(navArgument("placeId") { type = NavType.StringType })
            ) { backStackEntry ->

                val placeId = backStackEntry.arguments?.getString("placeId") ?: ""
                val place = allPlaces.find { it.id == placeId }

                if (place != null) {
                    PlaceDetailScreen(
                        place = place,
                        onBack = { navController.popBackStack() }
                    )
                }
            }
        }
    }

    // ---------- SEARCH PANEL ----------
    if (isSearchOpen) {
        ModalBottomSheet(
            onDismissRequest = { isSearchOpen = false },
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {

                Text("Buscar destinos", style = MaterialTheme.typography.titleMedium)

                Spacer(modifier = Modifier.height(12.dp))

                TextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    placeholder = { Text("Ej: Palacio Portales") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                filteredPlaces.forEach { place ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp)
                            .clickable {
                                isSearchOpen = false
                                navController.navigate(Screen.DetailPlace.create(place.id))
                            }
                    ) {
                        Text(place.name, style = MaterialTheme.typography.titleSmall)
                        Text(place.city, style = MaterialTheme.typography.bodySmall)
                        HorizontalDivider()
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = { isSearchOpen = false },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Cerrar")
                }
            }
        }
    }
}
