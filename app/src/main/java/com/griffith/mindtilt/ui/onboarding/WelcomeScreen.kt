// StudentName: Muhannad Ali
// StudentNr: 3102891

package com.griffith.mindtilt.ui.onboarding

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.griffith.mindtilt.ui.theme.LocalGradients

@Composable
fun WelcomeScreen(onNextClick: (String) -> Unit) {
    // Persist onboarding state
    var name by rememberSaveable { mutableStateOf("") }
    var error by rememberSaveable { mutableStateOf(false) }
    // Get gradient fron theme
    val gradientColors = LocalGradients.current.primary

    // Scaffold to handle system insets
    Scaffold (containerColor = Color.Transparent) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = Brush.verticalGradient(gradientColors))
                .padding(innerPadding)
        ) {
            // Whole screen scrolls to avoid layout compression on small devices
            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 120.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Decorative canvas element
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                ) {
                    val path = Path().apply {
                        moveTo(0f, size.height * 0.6f)
                        cubicTo(
                            size.width * 0.25f, size.height * 0.9f,
                            size.width * 0.75f, size.height * 0.35f,
                            size.width, size.height * 0.6f
                        )
                        lineTo(size.width, 0f)
                        lineTo(0f, 0f)
                        close()
                    }
                    drawPath(path, Color.White.copy(alpha = 0.18f))
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Header title with a subtle glow to make it visually stand out
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .background(
                                brush = Brush.radialGradient(
                                    listOf(
                                        Color.White.copy(alpha = 0.08f),
                                        Color.Transparent
                                    )
                                ),
                                shape = CircleShape
                            )
                    )
                    Text(
                        text = "MindTilt",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Medium,
                            letterSpacing = 0.5.sp
                        ),
                        modifier = Modifier.wrapContentWidth(Alignment.CenterHorizontally)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Short tagline container to improve readability over bright gradients
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .wrapContentWidth(Alignment.CenterHorizontally)
                        .background(Color.White.copy(alpha = 0.08f), shape = RoundedCornerShape(16.dp))
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "Boost your mood with short frequency sessions",
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = Color.White.copy(alpha = 0.95f),
                            fontWeight = FontWeight.Medium,
                            lineHeight = 30.sp
                        ),
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(40.dp))

                // Main input section styled as a card for visual separation
                Surface (
                    shape = RoundedCornerShape(28.dp),
                    tonalElevation = 12.dp,
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.25f),
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Text(
                            text = "Enter your name to start",
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = MaterialTheme.colorScheme.onBackground,
                                shadow = Shadow(
                                    color = Color.Black.copy(alpha = 0.3f),
                                    offset = Offset(1f, 1f),
                                    blurRadius = 2f
                                )
                            ),
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        // Input field for username
                        OutlinedTextField(
                            value = name,
                            // Always capitalize first letter, simple input validation UX
                            onValueChange = {
                                name = it.replaceFirstChar { c -> c.uppercase() }
                                error = false
                            },
                            label = { Text("Your Name") },
                            isError = error,
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(5.dp)
                        )
                        // Show error message if name is invalid (2-10 letters only)
                        if (error) {
                            Text(
                                text = "Name must be 2-10 letters",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

            }
            // Gradient next button navigates to BenefitsScreen (onboarding screen 2)
            Button(
                onClick = {
                    if (isValidName(name)) onNextClick(name) else error = true
                },
                modifier = Modifier
                    .padding(bottom = 24.dp)
                    .fillMaxWidth()
                    .height(54.dp)
                    .padding(horizontal = 24.dp)
                    .align(Alignment.BottomCenter),
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
                                listOf(
                                    Color(0xFF8E7DFF),
                                    Color(0xFFB295FF)
                                )
                            ),
                            shape = RoundedCornerShape(18.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Next",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = Color.White
                        )
                    )

                }
            }
        }

    }

}

// Boolean helper function to validate name
fun isValidName(input: String): Boolean {
    // Name must be between 2-10 letters
    val regex = "^[A-Za-z\\s]{2,10}$".toRegex()
    return input.trim().matches(regex)
}
