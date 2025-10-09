
package com.example.turismoapp.feature.navigation;

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Profile: Screen("profile")
    object Github: Screen("github")
    object Home    : Screen("home")
    object Cart    : Screen("cart")
    object Dollar: Screen("dollar")
    object PopularMovies: Screen("popularMovies")
    object Onboarding : Screen("onboarding")


}