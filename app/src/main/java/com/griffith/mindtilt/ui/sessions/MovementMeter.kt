// StudentName: Muhannad Ali
// StudentNr: 3102891

package com.griffith.mindtilt.ui.sessions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MovementMeter(
    movementLevel: Float
    ) {

    // Determine bar color and status text based on movement level
    // Color code the movement feedback (green/amber/red)
    val (statusText, barColor) = when {
        movementLevel < 0.2f -> "Perfect - Stay still" to Color(0xFF4CAF50)
        movementLevel < 0.5f -> "Small movements detected" to Color(0xFFFFC107)
        else -> "Please remain still" to Color(0xFFFF5252)
    }

    // Minimum fraction to keep bar visible when there is no movement, else show actual movement
    val fillFraction = movementLevel.coerceIn(0f, 1f).coerceAtLeast(0.05f)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Background bar showing total possible movement
        Box(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(24.dp)
                .background(Color.White.copy(alpha = 0.15f), RoundedCornerShape(12.dp))
        ) {
            // Foreground bar showing actual movement level
            Box(
                modifier = Modifier
                    .fillMaxWidth(fillFraction)
                    .height(24.dp)
                    .background(barColor, RoundedCornerShape(12.dp))
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Text feedback corresponding to current movement
        Text(
            text = statusText,
            color = barColor,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.35f),
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }

}