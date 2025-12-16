// StudentName: Muhannad Ali
// StudentNr: 3102891

package com.griffith.mindtilt.ui.sessions

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.griffith.mindtilt.ui.theme.LocalGradients
import kotlinx.coroutines.delay


@Composable
fun SessionScreen(
    mood: String,
    sessionDurationSec: Int = 60,
    onEndSession: () -> Unit,
    onSessionFinished: () -> Unit
) {
    val context = LocalContext.current
    // Manage ambient + isochronic tones during session
    val audioManager = remember { SessionAudioManager(context) }
    // Initialize accelerometer manager to track movement
    val accelerometerManager = remember { AccelerometerManager(context) }
    // Track device movement (0 = still, 1 = very moving)
    var movement by remember { mutableFloatStateOf(0f) }
    // Track elapsed session time to update progress bar
    var elapsedTime by remember { mutableFloatStateOf(0f) }

    val gradientColors = LocalGradients.current.primary


    // Start audio session, accelerometer, and increment elapsedTime in coroutine
    LaunchedEffect(Unit) {
        // Start the audio session (ambient + isochronic tones)
        audioManager.startSession(mood, sessionDurationSec)
        // Update movement state when the accelerometer detects motion
        accelerometerManager.onMovementDetected = { movementLevel -> movement = movementLevel }
        // Start listening to accelerometer updates
        accelerometerManager.start()

        // Update elapsed time every second to update progress bar
        while (elapsedTime < sessionDurationSec) {
            delay(1000L)
            elapsedTime++
        }
        onSessionFinished()
    }

    // Stop audio & accelerometer when composable leaves the screen
    DisposableEffect(Unit) {
        onDispose {
            accelerometerManager.stop()
            audioManager.stopSession()
        }
    }

    // Smooth animation to update progress bar
    val progress = animateFloatAsState(
        targetValue = elapsedTime / sessionDurationSec.toFloat(),
        animationSpec = tween(durationMillis = 300)
    ).value

    // Scaffold to handle system insets
    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = Brush.verticalGradient(gradientColors))
                .padding(innerPadding)
        ) {
            Column (modifier = Modifier.fillMaxSize()) {
                // Main layout for session content
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Spacer(modifier = Modifier.height(32.dp))

                    // Main card showing mood, wave visualizer, and session progress
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White.copy(alpha = 0.08f)
                        ),
                        shape = RoundedCornerShape(22.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(32.dp)
                        ) {
                            Box(
                                modifier = Modifier.size(160.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Box(
                                    modifier = Modifier
                                        .matchParentSize()
                                        .blur(12.dp)
                                        .background(
                                            Color.White.copy(alpha = 0.08f),
                                            CircleShape
                                        )
                                )
                                Text(
                                    text = mood,
                                    style = MaterialTheme.typography.headlineMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        letterSpacing = 0.5.sp
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.height(48.dp))
                            // Progress bar to show session progress
                            LinearProgressIndicator(
                                progress = { progress },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                                    .height(10.dp)
                                    .clip(RoundedCornerShape(50)),
                                color = gradientColors.last(),
                                trackColor = Color.White.copy(alpha = 0.25f),
                                strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
                            )

                            Spacer(modifier = Modifier.height(32.dp))

                            MeditationWaveVisualizer(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(120.dp),
                                amplitude = 18f,
                                waveCount = 6
                            )
                            Spacer(modifier = Modifier.height(32.dp))
                            // Shows a visual bar that fills according to movement level
                            MovementMeter(movementLevel = movement)

                        }
                    }

                    Spacer(modifier = Modifier.height(48.dp))

                    // End Session gradient button
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 16.dp)
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
                                    )
                                    .border(
                                        2.dp,
                                        Color.White.copy(alpha = 0.25f),
                                        RoundedCornerShape(18.dp)
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
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}