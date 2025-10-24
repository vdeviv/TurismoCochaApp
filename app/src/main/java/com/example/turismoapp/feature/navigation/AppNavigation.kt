package com.example.turismoapp.feature.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.turismoapp.feature.home.HomeScreen
import com.example.turismoapp.feature.home.HomeViewModel
import com.example.turismoapp.feature.login.presentation.LoginScreen
import com.example.turismoapp.feature.movie.presentation.PopularMoviesScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route // 👈 Esto es un String ("login")
    ) {
        // --------- Pantalla Login ----------
        composable(route = Screen.Login.route) {
            LoginScreen(
                onSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        // --------- Pantalla Home ----------
        composable(route = Screen.Home.route) {
            val vm: HomeViewModel = viewModel()
            val state = vm.ui.collectAsStateWithLifecycle()

            HomeScreen(
                state = state.value,
                onRetry = { vm.load() }
            )
        }

        // --------- Pantalla de Películas ----------
        composable(route = Screen.PopularMovies.route) {
            PopularMoviesScreen()
        }
    }
}
