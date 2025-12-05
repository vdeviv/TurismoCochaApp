package com.turismoapp.mayuandino.feature.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardTravel
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.turismoapp.mayuandino.ui.theme.PurpleMayu
import com.turismoapp.mayuandino.ui.theme.GrayText
import com.turismoapp.mayuandino.ui.theme.WhiteBackground

data class BottomNavItem(
    val label: String,
    val route: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

@Composable
fun BottomBar(
    navController: NavController,
    onSearchClick: () -> Unit
) {
    val items = listOf(
        BottomNavItem("Inicio", Screen.Home.route, Icons.Filled.Home),
        BottomNavItem("Calendario", Screen.Calendar.route, Icons.Filled.DateRange),
        BottomNavItem("Buscar", Screen.Search.route, Icons.Filled.Search),
        BottomNavItem("Paquetes", Screen.Packages.route, Icons.Filled.CardTravel),
        BottomNavItem("Perfil", Screen.Profile.route, Icons.Filled.Person)
    )

    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    NavigationBar(
        containerColor = WhiteBackground,
        tonalElevation = 12.dp
    ) {
        items.forEach { item ->
            val isSelected = currentRoute == item.route
            val isSearch = item.label == "Buscar"

            if (isSearch) {
                // FAB central para BUSCAR
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(top = 4.dp)
                        .height(60.dp),
                    contentAlignment = Alignment.TopCenter
                ) {
                    FloatingActionButton(
                        onClick = { onSearchClick() }, // ← YA NO NAVEGA
                        containerColor = PurpleMayu,
                        shape = CircleShape,
                        elevation = FloatingActionButtonDefaults.elevation(6.dp)
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.label,
                            tint = Color.White
                        )
                    }
                }
            } else {
                NavigationBarItem(
                    selected = isSelected,
                    onClick = {
                        if (currentRoute != item.route) {
                            navController.navigate(item.route) {
                                popUpTo(Screen.Home.route)
                                launchSingleTop = true
                            }
                        }
                    },
                    icon = { Icon(item.icon, contentDescription = item.label) },
                    label = { Text(item.label) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = PurpleMayu,
                        selectedTextColor = PurpleMayu,
                        unselectedIconColor = GrayText,
                        unselectedTextColor = GrayText,
                        indicatorColor = Color.Transparent
                    )
                )
            }
        }
    }
}
