// StudentName: Muhannad Ali
// StudentNr: 3102891

package com.griffith.mindtilt.ui.onboarding

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.griffith.mindtilt.ui.theme.LocalGradients

@Composable
fun BenefitsScreen(username: String, onNextClick: () -> Unit) {
    // Get gradient from theme
    val gradientColors = LocalGradients.current.primary
    // Create list of pairs(to iterate later and retrieve title and description)
    val benefits = listOf(
        "Reduced Stress" to "Short daily sessions help you feel calmer and less stressed",
        "Improved Focus" to "Boost focus and concentration with regular practice",
        "Better Mood" to "Short sessions increase positive feelings and happiness",
        "Better Sleep" to "Helps you fall asleep faster and sleep more deeply"
    )
    // Scaffold to handle system insets
    Scaffold (containerColor = Color.Transparent) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                // Background gradient
                .background(brush = Brush.verticalGradient(gradientColors))
                .padding(innerPadding)
        ) {
            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 120.dp)
            ) {

                // Decorative top wave shape canvas element
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
                        lineTo(0f,0f)
                        close()
                    }
                    drawPath(path,Color.White.copy(alpha = 0.18f))
                }

                // Welcome text with glowing radial background
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    // Glow circle to make it visually stand out
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .background(
                                brush = Brush.radialGradient(
                                    listOf(
                                        Color.White.copy(0.08f),
                                        Color.Transparent
                                    )
                                ),
                                shape = CircleShape
                            )
                    )
                    Text(
                        text = "Welcome, $username",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            color = Color.White,
                            letterSpacing = 0.6.sp
                        ),
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.CenterHorizontally)
                    )
                }

                // Subtitle under the welcome message
                Text(
                    text = "Explore the benefits of frequency sounds",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = Color.White.copy(alpha = 0.75f),
                    ),
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally)
                )

                // Card containing the list of benefits
                Surface(
                    shape = RoundedCornerShape(28.dp),
                    tonalElevation = 10.dp,
                    color = Color(0xFF1A1A1A).copy(alpha = 0.18f),
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        // Iterate each benefit item
                        benefits.forEach { (title, description) ->

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(vertical = 12.dp)
                            ) {
                                // Icon bubble with radial glow
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .background(
                                            brush = Brush.radialGradient(
                                                listOf(
                                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.85f),
                                                    Color.Transparent
                                                )
                                            ),
                                            shape = CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = Color.White,
                                    )
                                }

                                Spacer(modifier = Modifier.width(16.dp))

                                // Benefit title + description
                                Column {
                                    Text(
                                        text = title,
                                        style = MaterialTheme.typography.bodyLarge.copy(
                                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.98f),
                                            fontWeight = FontWeight.SemiBold
                                        ),
                                        modifier = Modifier.padding(bottom = 3.dp)
                                    )
                                    Text(
                                        text = description,
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.85f),
                                            fontWeight = FontWeight.Medium
                                        )
                                    )
                                }
                            }

                        }
                    }
                }
            }

            // Gradient continue button navigates to SummaryScreen (onbarding screen 3)
            Button(
                onClick = onNextClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 32.dp)
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
                        text = "Continue",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = Color.White
                        )
                    )
                }

            }

        }


    }

}