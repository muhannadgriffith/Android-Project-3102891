// StudentName: Muhannad Ali
// StudentNr: 3102891

package com.griffith.mindtilt.ui.home

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.griffith.mindtilt.ui.theme.MindTiltTheme

class HomeScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // Get the username from Intent passed from previous screen
        val username = intent.getStringExtra("username") ?: ""
        setContent {
            MindTiltTheme {
                // Pass username and placeholder lambdas to composable
                HomeScreenContent(
                    username,
                    onStartSession = {

                    },
                    onHistory = {

                    },
                    onSettings = {

                    }
                )
            }
        }
    }
}

@Composable
fun HomeScreenContent(username: String, onStartSession: () -> Unit, onHistory: () -> Unit, onSettings: () -> Unit) {
    // Using scaffold to handle edge to edge padding
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            // Greet the user
            Text(
                text = "Welcome, $username! 👋",
                fontSize = 28.sp,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Ready to start your meditation session?",
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(24.dp))

            // Button to starts a session
            Button(
                onClick = onStartSession,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Start Session")
            }
            Spacer(modifier = Modifier.height(24.dp))

            // History + Settings buttons
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedButton(onClick = onHistory) { Text("History") }
                OutlinedButton(onClick = onSettings) { Text("Settings") }
            }
        }
    }
}