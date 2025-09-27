package com.example.turismoapp.feature.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.turismoapp.feature.dollar.presentation.DollarScreen
import com.example.turismoapp.feature.login.presentation.CartScreen
import com.example.turismoapp.feature.login.presentation.LoginScreen
import com.example.turismoapp.feature.movie.presentation.PopularMoviesScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.PopularMovies.route

    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onSuccess = {
                    navController.navigate(Screen.Github.route) { // o Home/Profile
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Home.route) {
            // HomeScreen()
        }
        composable(Screen.Profile.route) {
            // ProfileScreen()
        }

        composable(Screen.Cart.route) {
            CartScreen()
        }

            composable(Screen.Dollar.route) {
                DollarScreen()
        }

        composable(Screen.PopularMovies.route) { PopularMoviesScreen() }
    }
}
