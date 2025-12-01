// StudentName: Muhannad Ali
// StudentNr: 3102891

package com.griffith.mindtilt.ui.sessions

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlin.math.sqrt

// Manages accelerometer sensor input to detect device movement
// Sends a movement level (0 = still, 1 = moving) to the UI
class AccelerometerManager(context: Context) : SensorEventListener {

    // Get system sensor manager
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    // Get the accelerometer sensor
    private val accelerometer: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    // Callback that will send movement level to the composable
    var onMovementDetected: ((Float) -> Unit)? = null

    // Gravity constant for reference, to detect movement
    private val GRAVITY = 9.81f

    // Store previous values for smoothing
    private var lastMagnitude = GRAVITY.toDouble()
    private val SMOOTHING_FACTOR = 0.8f

    // Start listening to the accelerometer
    fun start() {
        accelerometer?.also {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
    }

    // Stop listening when the composable leaves (to avoid battery drain)
    fun stop() {
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            // Calculate acceleration values along X, Y, Z axes
            val x = it.values[0]
            val y = it.values[1]
            val z = it.values[2]

            val magnitude = sqrt((x * x + y * y + z * z).toDouble())

            // Smooth the reading to reduce sudden jumps
            val smoothedMagnitude = (SMOOTHING_FACTOR * lastMagnitude) +
                    ((1 - SMOOTHING_FACTOR) * magnitude)
            lastMagnitude = smoothedMagnitude

            // Calculate deviation from gravity
            // When the device is still, magnitude = GRAVITY
            // When moving, magnitude deviates from GRAVITY
            val deviation = kotlin.math.abs(smoothedMagnitude - GRAVITY)

            // Convert deviation to a 0-1 scale for UI
            val maxDeviation = 5f
            val movement = (deviation / maxDeviation).coerceIn(0.0, 1.0).toFloat()
            // Send movement level to composable
            onMovementDetected?.invoke(movement)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }
}