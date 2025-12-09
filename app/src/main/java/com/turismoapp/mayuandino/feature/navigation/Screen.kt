
package com.turismoapp.mayuandino.feature.navigation;

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Profile: Screen("profile")
    object Notifications: Screen("notification")

    object EditProfile : Screen("edit_profile")
    object Favorites : Screen("favorites")
    object Trips : Screen("trips")
    object Settings : Screen("settings")
    object Language : Screen("language")

    object Github: Screen("github")
    object Home    : Screen("home")
    object Cart    : Screen("cart")
    object Dollar: Screen("dollar")
    object PopularMovies: Screen("popularMovies")
    object Onboarding : Screen("onboarding")
    object Calendar      : Screen("calendar")
    object Search        : Screen("search")
    object Packages      : Screen("packages")
    data object Register : Screen("register")
    object DetailPlace : Screen("detail/{placeId}") {
        fun create(placeId: String) = "detail/$placeId"
    }

}