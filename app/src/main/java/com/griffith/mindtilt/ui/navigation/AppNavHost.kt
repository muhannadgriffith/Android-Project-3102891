// StudentName: Muhannad Ali
// StudentNr: 3102891

package com.griffith.mindtilt.ui.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.griffith.mindtilt.ui.home.HomeScreen
import com.griffith.mindtilt.ui.onboarding.BenefitsScreen
import com.griffith.mindtilt.ui.onboarding.SummaryScreen
import com.griffith.mindtilt.ui.onboarding.WelcomeScreen

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: String = "welcome"
) {
    //
    // AnimatedNavHost container that manages navigation between composables
    // and supports animated transitions
    AnimatedNavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        // Onboarding screen 1: welcome
        composable(
            "welcome",
            enterTransition = { fadeIn(animationSpec = tween(durationMillis = 600)) },
            exitTransition = { fadeOut(animationSpec = tween(durationMillis = 600)) }
        ) {
            // Pass lambda to handle "next" button
            WelcomeScreen { username ->
                // Navigate to BenefitsScreen with the username
                navController.navigate("benefits/$username")
            }
        }

        // Onboarding screen 2: benefits
        composable(
            "benefits/{username}",
            enterTransition = { slideInHorizontally() },
            exitTransition = { slideOutHorizontally() }
        ) { backStackEntry ->
            // Extract the username from navigation argument
            val username = backStackEntry.arguments?.getString("username") ?: ""
            // Pass username to UI and a lambda to handle "next" button
            BenefitsScreen(username) {
                // Navigate to SummaryScreen with the username
                navController.navigate("summary/$username")
            }
        }

        // Onboarding screen 3: summary
        composable(
            "summary/{username}",
            enterTransition = { slideInHorizontally() },
            exitTransition = { slideOutHorizontally() }
        ) { backStackEntry ->
            // Extract the username from navigation argument
            val username = backStackEntry.arguments?.getString("username") ?: ""
            // Pass username to the UI and a lambda to handle "finish" button
            SummaryScreen(username) {
                // Navigate to Home screen with the username
                navController.navigate("home/$username") {
                    // Clear back stack so onboarding screens are removed
                    popUpTo("welcome") { inclusive = true }
                }
            }
        }

        // Main home screen after onboarding
        composable(
            "home/{username}",
            enterTransition = { fadeIn(animationSpec = tween(durationMillis = 600)) },
            exitTransition = { fadeOut(animationSpec = tween(durationMillis = 600)) }
        ) { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username") ?: ""
            // Pass callback lambdas for buttons
            HomeScreen(
                username = username,
                onStartSession = { },
                onHistory = { navController.navigate("history") },
                onSettings = { }
            )

        }
    }
}