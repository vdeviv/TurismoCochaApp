package com.example.turismoapp.feature.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.turismoapp.feature.login.presentation.CartScreen
import com.example.turismoapp.feature.login.presentation.LoginScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route

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
    }
}
