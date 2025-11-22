// StudentName: Muhannad Ali
// StudentNr: 3102891

package com.griffith.mindtilt

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.griffith.mindtilt.ui.navigation.AppNavHost
import com.griffith.mindtilt.ui.theme.MindTiltTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MindTiltTheme {
                val navController = rememberNavController()
                // Start navigation graph
                AppNavHost(navController)
            }
        }
    }
}
