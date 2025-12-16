// StudentName: Muhannad Ali
// StudentNr: 3102891

package com.griffith.mindtilt.ui.navigation

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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.griffith.mindtilt.ui.auth.AuthViewModel
import com.griffith.mindtilt.ui.auth.LoginScreen
import com.griffith.mindtilt.ui.auth.RegisterScreen
import com.griffith.mindtilt.ui.onboarding.BenefitsScreen
import com.griffith.mindtilt.ui.onboarding.SummaryScreen
import com.griffith.mindtilt.ui.onboarding.WelcomeScreen
import com.griffith.mindtilt.ui.theme.LocalGradients

// Navigation host for the onboardings
@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: String = "login",
    vm: AuthViewModel
) {
    val gradient = LocalGradients.current.primary

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.verticalGradient(gradient))
    ) {
        NavHost(navController = navController, startDestination = startDestination) {

            // Login screen, entry point
            composable("login") {
                LoginScreen(navController, vm)
            }

            // Register screen
            composable("register") {
                RegisterScreen(navController, vm)
            }

            // WelcomeScreen first onboarding after login/register
            composable(
                "welcome/{uid}",
                enterTransition = { fadeIn(animationSpec = tween(600)) },
                exitTransition = { fadeOut(animationSpec = tween(600)) }
            ) { backStackEntry ->
                val uid = backStackEntry.arguments?.getString("uid") ?: ""
                WelcomeScreen { name ->
                    // Save the user name in Firestore before moving to next screen
                    vm.setName(uid, name) { result ->
                        result.onSuccess {
                            navController.navigate("benefits/$uid/$name") {
                                popUpTo("welcome") { inclusive = true }
                            }
                        }.onFailure { e ->
                            println("Failed to save name: ${e.localizedMessage}")
                        }
                    }
                }
            }

            // BenefitScreen (onboarding 2)
            composable(
                "benefits/{uid}/{name}",
                enterTransition = { slideInHorizontally(initialOffsetX = { it }) + fadeIn(tween(300)) },
                exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) + fadeOut(tween(200)) }
            ) { backStackEntry ->
                val uid = backStackEntry.arguments?.getString("uid") ?: ""
                val name = backStackEntry.arguments?.getString("name") ?: ""
                BenefitsScreen(name) {
                    navController.navigate("summary/$uid/$name") {
                        popUpTo("benefits") { inclusive = true }
                    }
                }
            }

            // SummaryScreen (final onboarding), mark onboarding complete on finish
            composable(
                "summary/{uid}/{name}",
                enterTransition = { slideInHorizontally(initialOffsetX = { it }) + fadeIn(tween(300)) },
                exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) + fadeOut(tween(200)) }
            ) { backStackEntry ->
                val uid = backStackEntry.arguments?.getString("uid") ?: ""
                val name = backStackEntry.arguments?.getString("name") ?: ""
                SummaryScreen(name) {
                    vm.completeOnboarding(uid) {
                        navController.navigate("main/$name") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                }
            }

            // Main screen after onboarding with its own navigation
            composable(
                "main/{name}",
                enterTransition = { fadeIn(animationSpec = tween(600)) },
                exitTransition = { fadeOut(animationSpec = tween(600)) }
            ) { backStackEntry ->
                val name = backStackEntry.arguments?.getString("name") ?: ""
                val bottomNavController = rememberNavController()
                MainScreenNavHost(
                    name,
                    bottomNavController,
                    navController,
                    vm
                )
            }
        }
    }
}
