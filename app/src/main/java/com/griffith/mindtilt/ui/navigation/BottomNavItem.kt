// StudentName: Muhannad Ali
// StudentNr: 3102891

package com.griffith.mindtilt.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.filled.History

// Sealed class to ensure all bottom nav options are explicitly defined
// Each item has a route, icon, and label for use in the NavigationBar
sealed class BottomNavItem (
    val route: String,
    val icon: ImageVector,
    val label: String
) {
    object Home : BottomNavItem("home", Icons.Default.Home, "Home")
    object History : BottomNavItem("history", Icons.Default.History, "History")
    object Settings : BottomNavItem("settings", Icons.Default.Settings, "Settings")
}
// List of all bottom navigation items to iterate over in the NavigationBar
val bottomNavItems = listOf(
    BottomNavItem.Home,
    BottomNavItem.History,
    BottomNavItem.Settings
)