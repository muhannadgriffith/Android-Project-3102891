// StudentName: Muhannad Ali
// StudentNr: 3102891

package com.griffith.mindtilt.ui.navigation


import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.griffith.mindtilt.ui.history.HistoryScreen
import com.griffith.mindtilt.ui.home.HomeScreen
import com.griffith.mindtilt.ui.sessions.SessionScreen
import com.griffith.mindtilt.ui.settings.SettingsScreen

// Hosts the main bottom navigation and switches between home, history, and settings screens
@Composable
fun MainScreenNavHost(username: String, navController: NavHostController) {

    // Observe the current route to highlight the selected navigation item
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route
    // Scaffold to handle system insets
    Scaffold (
        // Bottom navigation bar with all defined items
        bottomBar = {
            NavigationBar (
                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
            ) {
                // Iterate through all bottom nav items to create buttons
                bottomNavItems.forEach { item ->
                    NavigationBarItem (
                        selected = currentRoute == item.route,
                        onClick = {
                            // Navigate to selected route, avoid stacking multiple destinations
                            navController.navigate(item.route) {
                                popUpTo("home") { inclusive = false }
                                launchSingleTop = true
                            }
                        },
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(item.label) }
                    )
                }
            }
        }
    ) { innerPadding ->
        // Host for the main screen content depending on the selected tab
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding)
        ) {

            composable("home") {
                HomeScreen(
                    username = username,
                    navController = navController
                )
            }

            composable("history") {
                HistoryScreen()
            }

            composable("settings") {
                SettingsScreen()
            }

            // SessionScreen
            composable(
                "session/{mood}",
                enterTransition = { fadeIn(animationSpec = tween(500)) },
                exitTransition = { fadeOut(animationSpec = tween(500)) }
            ) { backStackEntry ->
                val mood = backStackEntry.arguments?.getString("mood") ?: "Calm"
                SessionScreen(mood) {
                    navController.popBackStack() // Ends session and goes back to Home
                }
            }
        }
    }
}