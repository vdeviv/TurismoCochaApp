
package com.example.turismoapp.feature.navigation;

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Profile: Screen("profile")
    object Github: Screen("github")
    object Home    : Screen("home")
}