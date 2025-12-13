// StudentName: Muhannad Ali
// StudentNr: 3102891

package com.griffith.mindtilt.ui.sessions

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Color
import kotlin.math.sin

// Composable to draw a visual representation of a meditation wave
@Composable
fun MeditationWaveVisualizer(
    modifier: Modifier = Modifier,
    amplitude: Float = 20f,
    waveCount: Int = 3
) {
    // Animated phase value that drives the horizontal movement of the waves
    var phase by rememberSaveable { mutableFloatStateOf(0f) }

    // Moves the phase on every frame to create smooth animation
    // Using coroutine to not block main thread
    LaunchedEffect(Unit) {
        while (true) {
            withFrameNanos {
                phase += 0.015f
            }
        }
    }

    // Colors for layered wave effect
    val waveColors = listOf(
        Color(0xFFB4A5FF),
        Color(0xFF8E7DFF),
        Color(0xFF6C5DD3)
    )

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val centerY = height / 2f

        // Draw multiple sine waves with slight variations for depth
        repeat(waveCount) { i ->

            val waveColor = waveColors[i % waveColors.size]

            val localAmplitude = amplitude * (0.7f + i * 0.4f)
            val frequency = 0.014f + i * 0.008f
            val localPhase = phase * (1.1f + i * 0.15f)

            val path = Path().apply {
                moveTo(0f, centerY)
                for (x in 0 until width.toInt()) {
                    val y = centerY +
                            localAmplitude * sin(x * frequency + localPhase).toFloat()
                    lineTo(x.toFloat(), y)
                }
            }

            drawPath(
                path = path,
                color = waveColor,
                style = Stroke(width = 4f + (i * 1.5f))
            )
        }
    }
}

