package com.example.turismoapp.feature.navigation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

import com.example.turismoapp.feature.home.HomeScreen
import com.example.turismoapp.feature.home.HomeViewModel
import com.example.turismoapp.feature.login.presentation.LoginScreen
import com.example.turismoapp.feature.login.presentation.RegisterScreen
import com.example.turismoapp.feature.movie.presentation.PopularMoviesScreen
import com.example.turismoapp.feature.onboarding.presentation.OnboardingScreen
import com.example.turismoapp.feature.profile.presentation.EditProfileScreen
import com.example.turismoapp.feature.profile.presentation.ProfileScreen
import com.example.turismoapp.feature.splash.presentation.SplashScreen
import com.example.turismoapp.feature.search.presentation.SearchViewModel
import com.example.turismoapp.feature.search.presentation.PlaceDetailScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {

    val navController = rememberNavController()
    val searchVM: SearchViewModel = viewModel()
    val allPlaces by searchVM.allPlaces.collectAsState()

    var isSearchOpen by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    val filteredPlaces = allPlaces.filter {
        it.name.contains(searchText, ignoreCase = true) ||
                it.description.contains(searchText, ignoreCase = true) ||
                it.city.contains(searchText, ignoreCase = true)
    }

    val noBottomRoutes = setOf(
        Screen.Splash.route, Screen.Onboarding.route,
        Screen.Login.route, Screen.Register.route
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

            composable(Screen.Splash.route) {
                SplashScreen(onNavigateNext = {
                    navController.navigate(Screen.Onboarding.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                })
            }

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

            composable(Screen.Login.route) {
                LoginScreen(
                    onSuccess = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    },
                    onRegisterClick = {
                        navController.navigate(Screen.Register.route)
                    }
                )
            }

            composable(Screen.Register.route) {
                RegisterScreen(
                    onSuccess = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Register.route) { inclusive = true }
                        }
                    },
                    onLoginClick = {
                        navController.navigate(Screen.Login.route)
                    }
                )
            }

            composable(Screen.Home.route) {
                val vm: HomeViewModel = viewModel()
                val state = vm.ui.collectAsStateWithLifecycle()
                HomeScreen(state = state.value, onRetry = { vm.load() })
            }

            composable(Screen.Calendar.route) { /* TODO */ }
            composable(Screen.Packages.route) { /* TODO */ }

            composable(Screen.PopularMovies.route) { PopularMoviesScreen() }

            composable(Screen.Profile.route) {
                ProfileScreen(
                    onBack = { navController.popBackStack() },
                    onEditProfile = { navController.navigate(Screen.EditProfile.route) },
                    onFavorites = {},
                    onTrips = {},
                    onSettings = {},
                    onLanguage = {}
                )
            }

            composable(Screen.EditProfile.route) {
                EditProfileScreen(
                    onBack = { navController.popBackStack() },
                    onSave = { _, _, _, _ -> navController.popBackStack() }
                )
            }

            // ROUTE DE DETALLE
            composable(
                route = Screen.DetailPlace.route,
                arguments = listOf(navArgument("placeId") {
                    type = NavType.StringType
                })
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
                        Divider()
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
