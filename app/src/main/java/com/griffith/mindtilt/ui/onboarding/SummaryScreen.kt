// StudentName: Muhannad Ali
// StudentNr: 3102891

package com.griffith.mindtilt.ui.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.griffith.mindtilt.ui.theme.LocalGradients


@Composable
fun SummaryScreen(username:String, onFinishClick: () -> Unit) {
    // Get gradient from theme
    val gradientColors = LocalGradients.current.primary

    // Scaffold to handle system insets
    Scaffold(containerColor = Color.Transparent) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                // Gradient background
                .background(brush = Brush.verticalGradient(gradientColors))
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 120.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 150.dp),
                    contentAlignment = Alignment.Center
                ) {
                    // Soft radial glow behind the congratulation text
                    Box(
                        modifier = Modifier
                            .size(120.dp)
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
                    // Congratulate user on finished onboarding
                    Text(
                        text = "Congratulations, $username! \uD83C\uDF89",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Medium,
                            letterSpacing = 0.5.sp,
                            textAlign = TextAlign.Center,
                            lineHeight = 50.sp
                        ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(horizontal = 24.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Subtitle explaining user is ready to start
                Text(
                    text = "you're all set and ready to start",
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = Color.White.copy(alpha = 0.9f),
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .wrapContentWidth(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Card listing typical user improvements
                Surface(
                    shape = RoundedCornerShape(28.dp),
                    tonalElevation = 12.dp,
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.18f),
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .fillMaxWidth()
                ) {
                    Column (modifier = Modifier.padding(24.dp)) {

                        Text(
                            text = "What our users experience after a few sessions:",
                            style = MaterialTheme.typography.titleLarge.copy(
                                color = Color.White,
                                letterSpacing = 0.3.sp,
                                shadow = Shadow(
                                    color = Color.White.copy(alpha = 0.3f),
                                    blurRadius = 8f
                                )
                            ),
                            modifier = Modifier.padding(bottom = 24.dp)
                        )

                        // Group of benefits the user can expect
                        val benefits = listOf(
                            "Boost in focus and concentration",
                            "Improved mood and relaxation",
                            "Sleep better and wake up refreshed"
                        )

                        // Display each benefit with icon + text
                        benefits.forEach { benefit ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(vertical = 10.dp)
                            ) {
                                // Icon bubble with radial glow
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .background(
                                            brush = Brush.radialGradient(
                                                listOf(
                                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.9f),
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
                                        modifier = Modifier.size(22.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(14.dp))

                                // Benefit text
                                Text(
                                    text = benefit,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        color = Color.White,
                                        lineHeight = 22.sp
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Motivational final line
                        Text(
                            text = "Start your first session today and feel the difference!",
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = MaterialTheme.colorScheme.onBackground.copy(0.82f),
                                fontWeight = FontWeight.SemiBold,
                                letterSpacing = 0.2.sp,
                                fontSize = 20.sp
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Final gradient CTA button navigates to HomeScreen
                Button(
                    onClick = onFinishClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .padding(horizontal = 24.dp),
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
                            text = "Get Started",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = Color.White
                            )
                        )
                    }
                }

            }
        }
    }

}
