// StudentName: Muhannad Ali
// StudentNr: 3102891

package com.griffith.mindtilt.ui.onboarding

import android.R.attr.singleLine
import android.R.attr.top
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.griffith.mindtilt.ui.theme.MindTiltTheme

@Composable
fun WelcomeScreen(onNextClick: (String) -> Unit) {
    var name by rememberSaveable { mutableStateOf("") }
    var error by rememberSaveable { mutableStateOf(false) }
    // Using scaffold to handle edge to edge padding
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(32.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,

        ) {
            // App title
            Text(
                text = "MindTilt",
                fontSize =  40.sp,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(24.dp))

            // Subtitle
            Text(
                text = "Boost your mood with short frequency meditation sessions",
                fontSize =  18.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(32.dp))

            // Input field for username
            OutlinedTextField(
                value = name,
                // Always capitalize first letter
                onValueChange = {
                    name = it.replaceFirstChar { c ->
                        c.uppercase()
                    }
                    error = false // Reset error state
                },
                label = { Text("Enter your name") },
                isError = error,
                singleLine = true

            )
            // Show error message if name is invalid (2-10 letters only)
            if (error) {
                Text(
                    text = "Name must be 2-10 letters",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            Spacer(modifier = Modifier.height(24.dp))

            // Next button navigates to BenefitsScreen (onboarding screen 2)
            Button(
                onClick = {
                    if(isValidName(name)) {
                        onNextClick(name)
                    } else {
                        error = true
                    }
                },
                modifier = Modifier.padding(top = 24.dp)
            ) {
                Text("Next")
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
