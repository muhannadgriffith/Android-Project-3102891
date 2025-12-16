// StudentName: Muhannad Ali
// StudentNr: 3102891

package com.griffith.mindtilt

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.griffith.mindtilt.ui.auth.AuthViewModel
import com.griffith.mindtilt.ui.navigation.AppNavHost
import com.griffith.mindtilt.ui.theme.MindTiltTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MindTiltTheme {
                val navController = rememberNavController()
                // get the AuthViewModel instance
                val authViewModel: AuthViewModel = viewModel()
                // track which screen to start with
                var startDestination by remember { mutableStateOf<String?>(null) }
                var isCheckingAuth by remember { mutableStateOf(true) } // loading state

                // check if user is already logged in
                LaunchedEffect(authViewModel.isLoggedIn)  {
                    val currentUser = FirebaseAuth.getInstance().currentUser
                    if (currentUser != null) {
                        authViewModel.getUserData(currentUser.uid) { result ->
                            startDestination = result.fold(
                                onSuccess = { (name, onboarded) ->
                                    if (onboarded)
                                        "main/$name"
                                    else
                                        "welcome/${currentUser.uid}"
                                },
                                onFailure = { "login" } // Fallback
                            )
                            isCheckingAuth = false
                        }
                    } else {
                        // If not logged in, go to login
                        startDestination = "login"
                        isCheckingAuth = false
                    }
                }

                // Show loading screen while checking auth
                if (isCheckingAuth) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else {
                    startDestination?.let {
                        AppNavHost(navController = navController, startDestination = it, vm = authViewModel)
                    }
                }
            }
        }

    }
}
