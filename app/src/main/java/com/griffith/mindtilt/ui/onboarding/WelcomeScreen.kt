// StudentName: Muhannad Ali
// StudentNr: 3102891

package com.griffith.mindtilt.ui.onboarding

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
                onValueChange = { name = it },
                label = { Text("Enter your name") },
                singleLine = true

            )
            Spacer(modifier = Modifier.height(24.dp))

            // Next button navigates to BenefitsScreen (onboarding screen 2)
            Button(
                onClick = { onNextClick(name) },
                modifier = Modifier.padding(top = 24.dp)
            ) {
                Text("Next")
            }
        }
    }

}
