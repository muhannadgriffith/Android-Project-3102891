// StudentName: Muhannad Ali
// StudentNr: 3102891

package com.griffith.mindtilt.ui.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.griffith.mindtilt.ui.onboarding.BenefitsScreen
import com.griffith.mindtilt.ui.onboarding.SummaryScreen
import com.griffith.mindtilt.ui.onboarding.WelcomeScreen
import com.griffith.mindtilt.ui.theme.LocalGradients

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: String = "welcome"
) {
    val gradient = LocalGradients.current.primary

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.verticalGradient(gradient))
    ) {
        // AnimatedNavHost container to manage navigation between composables
        // and animated transitions
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
                enterTransition = {
                    slideInHorizontally(initialOffsetX = { it }) +
                            fadeIn(tween(300))
                },
                exitTransition = {
                    slideOutHorizontally(targetOffsetX = { -it }) +
                            fadeOut(tween(200))
                }
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
                enterTransition = {
                    slideInHorizontally(initialOffsetX = { it }) +
                            fadeIn(tween(300))
                },
                exitTransition = {
                    slideOutHorizontally(targetOffsetX = { -it }) +
                            fadeOut(tween(200))
                }
            ) { backStackEntry ->
                // Extract the username from navigation argument
                val username = backStackEntry.arguments?.getString("username") ?: ""
                // Pass username to the UI and a lambda to handle "finish" button
                SummaryScreen(username) {
                    // Navigate to Home screen with the username
                    navController.navigate("main/$username") {
                        // Clear back stack so onboarding screens are removed
                        popUpTo("welcome") { inclusive = true }
                    }
                }
            }

            // MainScreen after onboarding
            composable(
                "main/{username}",
                enterTransition = { fadeIn(animationSpec = tween(durationMillis = 600)) },
                exitTransition = { fadeOut(animationSpec = tween(durationMillis = 600)) }
            ) { backStackEntry ->
                val username = backStackEntry.arguments?.getString("username") ?: ""
                val bottomNavController = rememberNavController()
                MainScreenNavHost(username, bottomNavController)
            }
        }
    }

}