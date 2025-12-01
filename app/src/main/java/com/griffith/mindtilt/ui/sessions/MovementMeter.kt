// StudentName: Muhannad Ali
// StudentNr: 3102891

package com.griffith.mindtilt.ui.sessions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun MovementMeter(movementLevel: Float) {
    // Color changes based on movement level
    val barColor = when {
        movementLevel < 0.2f -> Color(0xFF4CAF50) // Green - good/still
        movementLevel < 0.5f -> Color(0xFFFFC107) // Amber - moderate movement
        else -> Color(0xFFFF5252) // Red - too much movement
    }
    // Background for the movement bar
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(24.dp)
            .background(Color.White.copy(alpha = 0.15f), RoundedCornerShape(12.dp))
    ) {
        // Represents the movement level
        Box(
            modifier = Modifier
                .fillMaxWidth(movementLevel.coerceIn(0f, 1f)) // 0f = empty, 1f = full width
                .height(24.dp)
                .background(barColor, RoundedCornerShape(12.dp))
        )
    }
}