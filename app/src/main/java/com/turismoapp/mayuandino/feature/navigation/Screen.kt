package com.turismoapp.mayuandino.feature.navigation

sealed class Screen(val route: String) {

    object Splash : Screen("splash")
    object Login : Screen("login")
    object Register : Screen("register")
    object Onboarding : Screen("onboarding")

    object Home : Screen("home")
    object Profile : Screen("profile")
    object EditProfile : Screen("edit_profile")

    object Favorites : Screen("favorites")
    object Trips : Screen("trips")
    object Settings : Screen("settings")
    object Language : Screen("language")

    object Github : Screen("github")
    object Dollar : Screen("dollar")
    object PopularMovies : Screen("popularMovies")

    // 🔥 FIX: usar el mismo nombre siempre
    object Notifications : Screen("notifications")

    // 🔥 CALENDAR
    object Calendar : Screen("calendar")

    // 🔥 SEARCH
    object Search : Screen("search")

    // 🔥 PACKAGES
    object Packages : Screen("packages")

    object PackageDetail : Screen("packageDetail/{id}") {
        fun create(id: String) = "packageDetail/$id"
    }

    object DetailPlace : Screen("detail/{placeId}") {
        fun create(placeId: String) = "detail/$placeId"
    }
}
