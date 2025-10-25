package com.example.turismoapp.feature.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.turismoapp.feature.home.HomeScreen
import com.example.turismoapp.feature.home.HomeViewModel
import com.example.turismoapp.feature.login.presentation.LoginScreen
import com.example.turismoapp.feature.login.presentation.RegisterScreen
import com.example.turismoapp.feature.movie.presentation.PopularMoviesScreen
import com.example.turismoapp.feature.onboarding.presentation.OnboardingScreen
import com.example.turismoapp.feature.splash.presentation.SplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val noBottomRoutes = setOf(
        Screen.Splash.route, Screen.Onboarding.route,
        Screen.Login.route, Screen.Register.route
    )
    val backStackEntry by navController.currentBackStackEntryAsState()
    val showBottomBar = backStackEntry?.destination?.route !in noBottomRoutes

    Scaffold(
        bottomBar = { if (showBottomBar) BottomBar(navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Splash.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Splash.route) {
                SplashScreen(
                    onNavigateNext = {
                        navController.navigate(Screen.Onboarding.route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    }
                )
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
                    onRegisterClick = { navController.navigate(Screen.Register.route) }
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
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    }
                )
            }

            composable(Screen.Home.route) {
                val vm: HomeViewModel = viewModel()
                val state = vm.ui.collectAsStateWithLifecycle()
                HomeScreen(state = state.value, onRetry = { vm.load() })
            }

            composable(Screen.Calendar.route) { /* … */ }
            composable(Screen.Search.route)   { /* … */ }
            composable(Screen.Packages.route) { /* … */ }
            composable(Screen.Profile.route)  { /* … */ }
            composable(Screen.PopularMovies.route) { PopularMoviesScreen() }
        }
    }
}
