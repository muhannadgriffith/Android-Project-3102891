// StudentName: Muhannad Ali
// StudentNr: 3102891

package com.griffith.mindtilt.ui.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.griffith.mindtilt.ui.theme.MindTiltTheme


class BenefitsScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // Get the username from Intent passed from previous screen
        val username = intent.getStringExtra("username") ?: ""
        setContent {
            MindTiltTheme {
                // Pass the username to the composable
                BenefitsScreenContent(
                    username,
                    onNextClick ={
                        // Navigate to SummaryScreen (onboarding screen 3) and pass username
                        val intent = Intent(this, SummaryScreen::class.java)
                        intent.putExtra("username", username)
                        startActivity(intent)
                        finish()
                    }
                )
            }
        }
    }
}

@Composable
fun BenefitsScreenContent(username: String, onNextClick: () -> Unit) {
    // Create list of pairs(to iterate later and retrieve title and description)
    val benefits = listOf(
        "Reduced Stress" to "Short daily sessions help you feel calmer and less stressed",
        "Improved Focus" to "Boost focus and concentration with regular practice",
        "Better Mood" to "Short sessions increase positive feelings and happiness",
        "Better Sleep" to "Helps you fall asleep faster and sleep more deeply"
    )
    // Using scaffold to handle edge to edge padding
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Greet user
            Text(
                text = "Welcome, $username!",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "You're about to start something special!",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Studies show that frequency sound sessions can help you achieve:",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(24.dp))

            // Display each benefit with an icon and description
            Column (horizontalAlignment = Alignment.Start) {
                benefits.forEach { (title, description) ->
                    Row(
                        modifier = Modifier.padding(bottom = 16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Checkmark",
                            tint = Color(0xFF4CAF50),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = title,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Text(
                                text = description,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                }

            }

            // Button to navigate to SummaryScreen (onbarding screen 3)
            Button(
                onClick = onNextClick,
                modifier = Modifier.padding(top = 24.dp)
            ) {
                Text("Next")
            }

        }
    }

}