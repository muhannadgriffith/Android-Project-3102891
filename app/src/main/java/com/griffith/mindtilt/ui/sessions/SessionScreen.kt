// StudentName: Muhannad Ali
// StudentNr: 3102891

package com.griffith.mindtilt.ui.sessions

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.griffith.mindtilt.ui.theme.LocalGradients

// Map each mood to a placeholder frequency (in Hz) that will play during the session
private val moodFrequencyMap = mapOf(
    "Calm" to 528,
    "Focus" to 432,
    "Energized" to 639,
    "Sleep" to 396
)

@Composable
fun SessionScreen(
    mood: String,
    onEndSession: () -> Unit
) {
    val context = LocalContext.current
    val gradientColors = LocalGradients.current.primary

    // Track device movement (0 = still, 1 = very moving)
    var movement by remember { mutableFloatStateOf(0f) }

    // Initialize accelerometer manager
    val accelerometerManager = remember { AccelerometerManager(context) }

    // Setup accelerometer callback and lifecycle
    DisposableEffect(Unit) {
        // Update movement whenever accelerometer detects motion
        accelerometerManager.onMovementDetected = { movementLevel ->
            movement = movementLevel
        }
        // Start listening to the accelerometer
        accelerometerManager.start()

        // Stop listening when this composable leaves the composition
        onDispose {
            accelerometerManager.stop()
        }
    }

    // Scaffold to handle system insets
    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = Brush.verticalGradient(gradientColors))
                .padding(innerPadding)
        ) {
            // Main layout for session content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Spacer(modifier = Modifier.height(32.dp))

                // Session title with subtle glow
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .background(
                            brush = Brush.radialGradient(
                                listOf(Color.White.copy(alpha = 0.08f), Color.Transparent)
                            ),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "$mood Session",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Medium,
                            color = Color.White,
                            letterSpacing = 0.5.sp
                        ),
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Frequency info card
                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.2f),
                    tonalElevation = 12.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Playing Frequency",
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = Color.White.copy(alpha = 0.8f),
                                fontWeight = FontWeight.Medium
                            )
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "${moodFrequencyMap[mood]} Hz",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 42.sp
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Movement meter section
                Text(
                    text = "Movement Detected",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Medium
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Shows a visual bar that fills according to movement level
                MovementMeter(movement)

                // Show warning if moving too much
                Spacer(modifier = Modifier.height(8.dp))

                // Textual feedback to guide the user to stay still
                val movementStatus = when {
                    movement < 0.2f -> "Perfect - Stay still"
                    movement < 0.5f -> "Small movements detected"
                    else -> "Please remain still"
                }

                // Color code the movement feedback (green/amber/red)
                val statusColor = when {
                    movement < 0.2f -> Color(0xFF4CAF50)
                    movement < 0.5f -> Color(0xFFFFC107)
                    else -> Color(0xFFFF5252)
                }

                Text(
                    text = movementStatus,
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = statusColor,
                        fontWeight = FontWeight.SemiBold
                    )
                )

                Spacer(modifier = Modifier.height(48.dp))

                // End Session gradient button
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
                        onClick = onEndSession,
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
                                text = "End Session",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    color = Color.White,
                                    fontSize = 18.sp
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}