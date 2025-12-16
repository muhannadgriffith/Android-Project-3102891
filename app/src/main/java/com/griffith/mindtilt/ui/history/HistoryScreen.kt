// StudentName: Muhannad Ali
// StudentNr: 3102891

package com.griffith.mindtilt.ui.history

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.griffith.mindtilt.ui.auth.AuthViewModel
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
@Composable
fun HistoryScreen(authViewModel: AuthViewModel) {
    val currentUser = FirebaseAuth.getInstance().currentUser ?: return
    // Holds the list of saved sessions
    var sessions by remember { mutableStateOf<List<AuthViewModel.Session>>(emptyList()) }
    // Track expanded card
    var expandedSessionId by remember { mutableStateOf<String?>(null) }

    // Fetch user's sessions using coroutine
    LaunchedEffect(Unit) {
        authViewModel.getSessions(currentUser.uid) { result ->
            result.onSuccess { sessions = it }
        }
    }

    // Show a message if the user has no saved sessions
    if (sessions.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No saved sessions yet",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White.copy(alpha = 0.8f)
            )
        }
    } else {
        // Scrollable list of saved sessions
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Show each session as a clickable card
            items(sessions) { session ->
                val isExpanded = session.id == expandedSessionId

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            // Expand or collapse the selected session
                            expandedSessionId =
                                if (isExpanded) null else session.id
                        },
                    shape = RoundedCornerShape(18.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                ) {
                    // Gradient background for the session card
                    Box(
                        modifier = Modifier
                            .background(
                                brush = Brush.horizontalGradient(
                                    listOf(Color(0xFF8E7DFF), Color(0xFFB295FF))
                                )
                            )
                            .padding(16.dp)
                    ) {
                        Column {
                            // Show mood and date of the session
                            Text(
                                session.mood,
                                style = MaterialTheme.typography.headlineSmall,
                                color = Color.White
                            )
                            Text(
                                SimpleDateFormat(
                                    "dd MMM yyyy HH:mm",
                                    Locale.getDefault()
                                ).format(session.timestamp),
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.7f)
                            )

                            // Show note and close button when expanded
                            if (isExpanded) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    session.note.ifEmpty { "No note provided" },
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.height(8.dp))

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(48.dp)
                                        .background(
                                            brush = Brush.horizontalGradient(
                                                listOf(Color(0xFF8E7DFF), Color(0xFFB295FF))
                                            ),
                                            shape = RoundedCornerShape(16.dp)
                                        )
                                ) {
                                    TextButton(
                                        onClick = { expandedSessionId = null },
                                        modifier = Modifier.fillMaxSize()
                                    ) {
                                        Text(
                                            text = "Close",
                                            color = Color.White,
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}