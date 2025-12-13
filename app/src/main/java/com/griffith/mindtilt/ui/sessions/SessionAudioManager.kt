// StudentName: Muhannad Ali
// StudentNr: 3102891

package com.griffith.mindtilt.ui.sessions

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import kotlin.math.PI
import kotlin.math.sin
import kotlin.math.cos
import com.griffith.mindtilt.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class SessionAudioManager(private val context: Context) {

    // MediaPlayer for handling the ambient background audio for the session
    private var ambientPlayer: MediaPlayer? = null
    // AudioTrack used for the isochronic tone (the pulsing tone)
    private var toneTrack: AudioTrack? = null
    // For scheduling the stopSession after the session duration
    private val handler = Handler(Looper.getMainLooper())
    // Reference to the active volume fade coroutine so it can be cancelled if needed
    private var fadeJob: Job? = null

    var ambientVolume = 1f
    var toneVolume = 0.015f
    private var sessionDurationSec = 60

    // Map moods to audio resources (ambient music)
    private val moodToAmbientRes = mapOf(
        "Calm" to R.raw.ambient_calm,
        "Focus" to R.raw.ambient_focus,
        "Energized" to R.raw.ambient_energized,
        "Sleep" to R.raw.ambient_sleep,
        "Spiritual" to R.raw.ambient_spiritual,
        "Creative" to R.raw.ambient_creative
    )

    // Map moods to carrier frequencies for isochronic tones (Hz)
    val moodToCarrier = mapOf(
        "Calm" to 432.0,
        "Focus" to 528.0,
        "Energized" to 639.0,
        "Sleep" to 174.0,
        "Spiritual" to 852.0,
        "Creative" to 639.0
    )

    // Map moods to pulse frequencies (how fast the tone envelopes)
    val moodToPulse = mapOf(
        "Calm" to 6.0,
        "Focus" to 10.0,
        "Energized" to 14.0,
        "Sleep" to 4.0,
        "Spiritual" to 8.0,
        "Creative" to 12.0
    )

    fun startSession(mood: String, durationSec: Int = 60) {
        stopSession() // Clean up any previous session
        sessionDurationSec = durationSec

        // Ambient music setup
        val ambientRes = moodToAmbientRes[mood] ?: moodToAmbientRes["Calm"]!!
        ambientPlayer = MediaPlayer.create(context, ambientRes).apply {
            isLooping = true
            setVolume(0f, 0f) // start silent, before fade in
            start()
        }
        fadeMediaPlayerVolume(ambientPlayer, 0f, ambientVolume, 800L) // Fade in

        // Isochronic tone setup
        val carrier = moodToCarrier[mood] ?: 432.0
        val pulse = moodToPulse[mood] ?: 6.0
        toneTrack = createIsochronicAudioTrack(carrier, pulse, sessionDurationSec).also { track ->
            try { track.setVolume(toneVolume) } catch (e: NoSuchMethodError) { track.setStereoVolume(toneVolume, toneVolume) }
            track.play()
        }
        // Schedule automatic stop at end of session
        handler.postDelayed({ stopSession() }, (sessionDurationSec * 1000L))
    }

    fun stopSession() {
        fadeJob?.cancel() // Cancel any ongoing fade coroutine
        // Fade out ambient music, then stop/release player
        ambientPlayer?.let { mp ->
            fadeMediaPlayerVolume(mp, ambientVolume, 0f, 400L) {
                try { mp.stop() } catch (_: Exception) {}
                mp.release()
            }
        }
        ambientPlayer = null
        // Stop and release isochronic tone
        toneTrack?.let { track ->
            try { track.stop() } catch (_: Exception) {}
            track.release()
        }
        toneTrack = null

        // Remove any pending handler callbacks
        handler.removeCallbacksAndMessages(null)
    }

    // For creating the isochronic tone
    private fun createIsochronicAudioTrack(carrierHz: Double, pulseHz: Double, durationSeconds: Int): AudioTrack {
        val sampleRate = 44100
        val sampleCount = durationSeconds * sampleRate
        val buffer = ShortArray(sampleCount)

        // Fill the buffer with a sine wave and apply a pulse envelope to create the isochronic effect
        for (i in 0 until sampleCount) {
            val angle = 2.0 * PI * carrierHz * i / sampleRate
            val sine = sin(angle)
            val envelope = 0.5 * (1 - cos(2.0 * PI * pulseHz * i / sampleRate))
            buffer[i] = (Short.MAX_VALUE * sine * envelope).toInt().toShort()
        }

        val byteCount = buffer.size * 2
        return AudioTrack.Builder()
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
            )
            .setAudioFormat(
                AudioFormat.Builder()
                    .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                    .setSampleRate(sampleRate)
                    .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                    .build()
            )
            .setBufferSizeInBytes(byteCount)
            .setTransferMode(AudioTrack.MODE_STATIC)
            .build()
            .apply { write(buffer, 0, buffer.size) }
    }

    // For fading the volume of the MediaPlayer
    private fun fadeMediaPlayerVolume(
        mp: MediaPlayer?,
        from: Float,
        to: Float,
        durationMillis: Long,
        onComplete: (() -> Unit)? = null
    ) {
        if (mp == null) { onComplete?.invoke(); return }
        fadeJob?.cancel() // cancel previous fade if any
        fadeJob = CoroutineScope(Dispatchers.Default).launch {
            val steps = 20
            val delta = (to - from) / steps
            var current = from
            val delayMs = durationMillis / steps

            // Gradually adjust volume over time
            repeat(steps) {
                current += delta
                mp.setVolume(current.coerceIn(0f, 1f), current.coerceIn(0f, 1f))
                delay(delayMs)
            }
            // Notify completion to main thread
            withContext(Dispatchers.Main) { onComplete?.invoke() }
        }
    }
}
