// StudentName: Muhannad Ali
// StudentNr: 3102891

package com.griffith.mindtilt

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import com.griffith.mindtilt.ui.onboarding.WelcomeScreen
import com.griffith.mindtilt.ui.theme.MindTiltTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // Start WelcomeScreen (onboarding screen 1)
        startActivity(Intent(this, WelcomeScreen::class.java))
        finish()
    }
}
