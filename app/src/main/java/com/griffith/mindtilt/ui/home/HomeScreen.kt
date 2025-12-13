// StudentName: Muhannad Ali
// StudentNr: 3102891

package com.griffith.mindtilt.ui.home

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.griffith.mindtilt.ui.theme.LocalGradients

// List of moods the user can choose from before starting a session
private val moods = listOf("Calm", "Focus", "Energized", "Sleep", "Creative", "Spiritual")

// Gradient colors for each mood to visually differentiate them
private val moodGradients = mapOf(
    "Calm" to listOf(Color(0xFFA6CEE3).copy(alpha = 0.7f), Color(0xFF1F78B4).copy(alpha = 0.7f)),
    "Focus" to listOf(Color(0xFFFFD580).copy(alpha = 0.7f), Color(0xFFFF9F80).copy(alpha = 0.7f)),
    "Energized" to listOf(Color(0xFFFFE08C).copy(alpha = 0.7f), Color(0xFFFFB347).copy(alpha = 0.7f)),
    "Sleep" to listOf(Color(0xFF6A5ACD).copy(alpha = 0.7f), Color(0xFF483D8B).copy(alpha = 0.7f)),
    "Creative" to listOf(Color(0xFFDA70D6).copy(alpha = 0.7f), Color(0xFF9932CC).copy(alpha = 0.7f)),
    "Spiritual" to listOf(Color(0xFF7FFFD4).copy(alpha = 0.7f), Color(0xFF20B2AA).copy(alpha = 0.7f))
)

@Composable
fun HomeScreen(
    username: String,
    navController: NavController
) {
    val gradientColors = LocalGradients.current.primary

    // State to store the currently selected mood
    var selectedMood by rememberSaveable { mutableStateOf<String?>(null) }

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

                // Prompt asking user to choose mood
                Text(
                    text = "What mood are you seeking today?",
                    modifier = Modifier.padding(top = 12.dp),
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = Color.White.copy(alpha = 0.9f)
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Grid to display all the mood options
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth().weight(1f)
                ) {
                    // Loop through moods and display each as a clickable Box
                    items(moods) { mood ->
                        val isSelected = mood == selectedMood
                        Box(
                            modifier = Modifier
                                .height(120.dp)
                                .border(
                                    width = if (isSelected) 3.dp else 0.dp,
                                    color = if (isSelected) Color.White else Color.Transparent,
                                    shape = RoundedCornerShape(20.dp)
                                )
                                .background(
                                    brush = Brush.horizontalGradient(
                                        moodGradients[mood] ?: listOf(Color.Gray, Color.DarkGray)
                                    ),
                                    shape = RoundedCornerShape(20.dp)
                                )
                                .clickable { selectedMood = mood }, // Select mood on click
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = mood,
                                color = Color.White,
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Button that navigates into the meditation session screen with selected mood
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
                        contentPadding = PaddingValues(),
                        enabled = selectedMood != null, // Disable button until user selects a mood
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(54.dp)
                                .background( // Gradient background if a mood is selected, else a darker background
                                    brush = if (selectedMood != null)
                                        Brush.horizontalGradient(listOf(Color(0xFF8E7DFF), Color(0xFFB295FF)))
                                    else
                                        Brush.horizontalGradient(listOf(Color(0xFF4A4A4A), Color(0xFF2F2F2F))),
                                    shape = RoundedCornerShape(18.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (selectedMood != null) "Start $selectedMood Session" else "Select a Mood",
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