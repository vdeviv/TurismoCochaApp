package com.turismoapp.mayuandino.feature.navigation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

import androidx.lifecycle.viewmodel.compose.viewModel

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
import com.turismoapp.mayuandino.feature.calendar.data.repository.CalendarRepository
import com.turismoapp.mayuandino.feature.calendar.presentation.components.EventDetailScreen

// PACKAGES
import com.turismoapp.mayuandino.feature.packages.presentation.PackagesScreen
import com.turismoapp.mayuandino.feature.packages.presentation.PackagesViewModel
import com.turismoapp.mayuandino.feature.packages.presentation.PackageDetailScreen

import org.koin.androidx.compose.koinViewModel
import org.koin.androidx.compose.get

// Notifications
import com.turismoapp.mayuandino.feature.notification.presentation.NotificationScreen
import com.turismoapp.mayuandino.feature.notification.presentation.NotificationViewModel
import com.turismoapp.mayuandino.feature.notification.presentation.NotificationViewModelFactory

import android.app.Application


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    // ---------------------------------------
    // SEARCH VIEWMODEL
    // ---------------------------------------
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

            // ---------------- HOME + SYNC ----------------
            composable(Screen.Home.route) {

                val calendarRepo: CalendarRepository = get()

                LaunchedEffect(Unit) {
                    try {
                        calendarRepo.syncCalendarEvents()
                    } catch (_: Exception) { }
                }

                HomeScreen(
                    onProfileClick = { navController.navigate(Screen.Profile.route) },
                    onNotificationClick = { navController.navigate(Screen.Notifications.route) },
                    onPlaceClick = { id ->
                        navController.navigate(Screen.DetailPlace.create(id))
                    }
                )
            }

            // ---------------- NOTIFICATIONS ----------------
            composable(Screen.Notifications.route) {
                val app = LocalContext.current.applicationContext as Application
                val vm: NotificationViewModel = viewModel(
                    factory = NotificationViewModelFactory(app)
                )

                NotificationScreen(
                    viewModel = vm,
                    onBackClick = { navController.popBackStack() }
                )
            }

            // ----------------------- CALENDAR -----------------------
            composable(Screen.Calendar.route) {

                val vm: CalendarViewModel = koinViewModel()
                val repo: CalendarRepository = get()

                LaunchedEffect(Unit) {
                    repo.syncCalendarEvents()
                }

                CalendarScreen(navController = navController, viewModel = vm)
            }

// ----------------------- EVENT DETAIL -----------------------
            composable(
                route = "eventDetail/{eventId}",
                arguments = listOf(
                    navArgument("eventId") { type = NavType.StringType }
                )
            ) { backStackEntry ->

                val eventId = backStackEntry.arguments?.getString("eventId") ?: ""

                val vm: CalendarViewModel = koinViewModel()

                EventDetailScreen(
                    eventId = eventId,
                    viewModel = vm
                )
            }


            // ---------------- MOVIES ----------------
            composable(Screen.PopularMovies.route) {
                PopularMoviesScreen()
            }


            // ---------------- PROFILE ----------------
            composable(Screen.Profile.route) {
                // 💡 Aquí se usaba onDeleteAccount y onSignOut

                // Define la acción de cierre de sesión
                val signOutAction: () -> Unit = {
                    // Navegar a Login y limpiar la pila
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }

                // Define la acción de eliminación de cuenta (misma lógica de navegación)
                val deleteAccountAction: () -> Unit = {
                    // Navegar a Login y limpiar la pila
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }

                ProfileScreen(
                    onEditProfileClick = { navController.navigate(Screen.EditProfile.route) },

                    // ⬇️ PASAMOS LA ACCIÓN DE NAVEGACIÓN A LA PANTALLA
                    onSignOut = signOutAction,
                    onDeleteAccount = deleteAccountAction
                )
            }

            // ---------------- EDIT PROFILE ----------------
            composable(Screen.EditProfile.route) {
                EditProfileScreen(
                    onBack = { navController.popBackStack() }
                )
            }

            // ---------------- PACKAGES ----------------
            composable(Screen.Packages.route) {
                val vm: PackagesViewModel = koinViewModel()
                PackagesScreen(
                    viewModel = vm,
                    onPackageClick = { id ->
                        navController.navigate(Screen.PackageDetail.create(id))
                    }
                )
            }

            // ---------------- PACKAGE DETAIL ----------------
            composable(
                route = Screen.PackageDetail.route,
                arguments = listOf(navArgument("id") { type = NavType.StringType })
            ) { backStackEntry ->

                val id = backStackEntry.arguments?.getString("id") ?: ""
                val vm: PackagesViewModel = koinViewModel()
                val state by vm.state.collectAsState()

                val pkg = state.packages.find { it.id == id }

                if (pkg != null) {
                    PackageDetailScreen(
                        pkg = pkg,
                        onBack = { navController.popBackStack() }
                    )
                }
            }

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

    // ---------------- SEARCH PANEL ----------------
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
