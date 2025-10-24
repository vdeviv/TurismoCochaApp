package com.example.turismoapp.feature.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.turismoapp.feature.home.HomeScreen
import com.example.turismoapp.feature.home.HomeViewModel
import com.example.turismoapp.feature.login.presentation.LoginScreen
import com.example.turismoapp.feature.login.presentation.RegisterScreen
import com.example.turismoapp.feature.movie.presentation.PopularMoviesScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomBar(navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Login.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            // Login → al éxito navega al Home
            composable(Screen.Login.route) {
                LoginScreen(
                    onSuccess = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    },
                    onRegisterClick = { navController.navigate(Screen.Register.route) } // <-- NAVEGA A REGISTER
                )
            }

            // Register → al éxito navega al Home
            composable(Screen.Register.route) { // <-- NUEVA RUTA DE REGISTRO
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
                    } // <-- NAVEGA A LOGIN
                )
            }

            // Home con ViewModel
            composable(Screen.Home.route) {
                val vm: HomeViewModel = viewModel()
                val state = vm.ui.collectAsStateWithLifecycle()
                HomeScreen(
                    state = state.value,
                    onRetry = { vm.load() }
                )
            }

            // Rutas de la barra
            composable(Screen.Calendar.route) { Text("Calendario") }
            composable(Screen.Search.route)   { Text("Buscar / Explorar") }
            composable(Screen.Packages.route) { Text("Paquetes turísticos") }
            composable(Screen.Profile.route)  { Text("Perfil") }

            // Extra
            composable(Screen.PopularMovies.route) { PopularMoviesScreen() }
        }
    }
}
