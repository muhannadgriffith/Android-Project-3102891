// StudentName: Muhannad Ali
// StudentNr: 3102891

package com.griffith.mindtilt.ui.home

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.griffith.mindtilt.ui.theme.LocalGradients

// List of moods the user can choose from before starting a session
private val moods = listOf("Calm", "Focus", "Energized", "Sleep")

@Composable
fun HomeScreen(
    username: String,
    navController: NavController
) {
    val gradientColors = LocalGradients.current.primary
    var selectedMood by remember { mutableStateOf(moods.first()) }

    // Scaffold to handle system insets
    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = Brush.verticalGradient(gradientColors))
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(32.dp))

                // Always display a friendly greeting to the user
                Text(
                    text = "Welcome, $username! 👋",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        color = Color.White,
                        fontSize = 28.sp
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Simple instructions for the user
                Text(
                    text = "Select your mood for this session",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 18.sp
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Mood selection buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    moods.forEach { mood ->
                        // Display a clickable box for each mood
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 4.dp)
                                .height(50.dp)
                                .background(
                                    color = if (mood == selectedMood) Color.White.copy(alpha = 0.2f) // highlight selected mood
                                    else Color.White.copy(alpha = 0.1f),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .clickable { selectedMood = mood },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = mood,
                                color = Color.White,
                                style = MaterialTheme.typography.titleMedium.copy(fontSize = 16.sp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                /// Button that navigates into the meditation session screen with selected mood
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(
                            elevation = 8.dp,
                            shape = RoundedCornerShape(18.dp),
                            spotColor = Color.Black.copy(alpha = 0.3f)
                        )
                ) {
                    Button(
                        onClick = {
                            navController.navigate("session/$selectedMood")
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                            .border(
                                width = 2.dp,
                                color = Color.White.copy(alpha = 0.3f),
                                shape = RoundedCornerShape(18.dp)
                            ),
                        shape = RoundedCornerShape(18.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        contentPadding = PaddingValues()
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(54.dp)
                                .background(
                                    brush = Brush.horizontalGradient(
                                        listOf(Color(0xFF8E7DFF), Color(0xFFB295FF))
                                    ),
                                    shape = RoundedCornerShape(18.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Start Session",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    color = Color.White,
                                    fontSize = 18.sp
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
                // Save the current context
                val context = LocalContext.current
                // Implicit intent to share progress (This will be moved in later milestone to be displayed after user finishes a session)
                Button(
                    onClick = {
                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_SUBJECT, "MindTilt Meditation")
                            putExtra(Intent.EXTRA_TEXT, "I just finished a meditation session with MindTilt")
                        }
                        context.startActivity(Intent.createChooser(shareIntent, "Share via"))
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Share Progress")
                }
            }

        }
    }

}