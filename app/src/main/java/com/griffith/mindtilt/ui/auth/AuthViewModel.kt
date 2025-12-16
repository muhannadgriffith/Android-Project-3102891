// StudentName: Muhannad Ali
// StudentNr: 3102891

package com.griffith.mindtilt.ui.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.firestore
import com.google.firebase.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

// ViewModel for authentication and user data
// Handles asynchronous operations
class AuthViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val db = Firebase.firestore

    var isLoggedIn by mutableStateOf(auth.currentUser != null)
        private set

    // Register user with email + password
    fun register(email: String, password: String, onResult: (Result<String>) -> Unit) {
        viewModelScope.launch {
            try {
                val result = auth.createUserWithEmailAndPassword(email, password).await()
                val uid = result.user?.uid ?: throw Exception("UID is null")

                // Save user info in Firestore
                db.collection("users").document(uid)
                    .set(
                        mapOf(
                            "email" to email,
                            "name" to "",
                            "onboarded" to false
                        )
                    ).await()
                onResult(Result.success(uid))
            } catch (e: Exception) {
                val message = when (e) { // Handle different exceptions
                    is FirebaseAuthUserCollisionException -> "This email is already in use"
                    is FirebaseAuthWeakPasswordException -> "Password is too weak"
                    else -> e.message ?: "Registration failed"
                }
                onResult(Result.failure(Exception(message)))
            }
        }
    }

    // Login user with email + password
    fun login(email: String, password: String, onResult: (Result<String>) -> Unit) {
        viewModelScope.launch {
            try {
                val result = auth.signInWithEmailAndPassword(email, password).await()
                val uid = result.user?.uid ?: throw Exception("UID is null")
                isLoggedIn = true
                onResult(Result.success(uid))
            } catch (e: Exception) {
                val message = when (e) { // Handle different exceptions
                    is FirebaseAuthInvalidUserException -> "No user found with this email"
                    is FirebaseAuthInvalidCredentialsException -> "Wrong password"
                    else -> e.message ?: "Login failed"
                }
                onResult(Result.failure(Exception(message)))
            }
        }
    }

    // Get user data (name + onboarded status)
    fun getUserData(uid: String, onResult: (Result<Pair<String, Boolean>>) -> Unit) {
        viewModelScope.launch {
            try {
                val doc = db.collection("users").document(uid).get().await()
                val name = doc.getString("name") ?: ""
                val onboarded = doc.getBoolean("onboarded") ?: false
                onResult(Result.success(name to onboarded)) // Return name and onboarded status
            } catch (e: Exception) {
                onResult(Result.failure(e))
            }
        }
    }

    // Set user name (in welcome screen)
    fun setName(uid: String, name: String, onResult: (Result<Unit>) -> Unit) {
        viewModelScope.launch {
            try {
                db.collection("users").document(uid).update("name", name).await()
                onResult(Result.success(Unit))
            } catch (e: Exception) {
                onResult(Result.failure(e))
            }
        }
    }

    // Complete onboarding (in summary screen)
    fun completeOnboarding(uid: String, onResult: (Result<Unit>) -> Unit) {
        viewModelScope.launch {
            try {
                db.collection("users").document(uid).update("onboarded", true).await()
                onResult(Result.success(Unit))
            } catch (e: Exception) {
                onResult(Result.failure(e))
            }
        }
    }

    // Save session with mood and note
    fun saveSession(
        uid: String,
        mood: String,
        note: String,
        onResult: (Result<Unit>) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val sessionData = mapOf(
                    "mood" to mood,
                    "note" to note,
                    "timestamp" to System.currentTimeMillis()
                )
                db.collection("users")
                    .document(uid)
                    .collection("sessions")
                    .add(sessionData)
                    .await()
                onResult(Result.success(Unit))
            } catch (e: Exception) {
                onResult(
                    Result.failure(
                        Exception("Failed to save session. Please try again")
                    )
                )
            }
        }
    }

    // Get user's sessions
    fun getSessions(uid: String, onResult: (Result<List<Session>>) -> Unit) {
        viewModelScope.launch {
            try {
                val snapshot = db.collection("users")
                    .document(uid)
                    .collection("sessions")
                    .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
                    .get()
                    .await()
                val sessions = snapshot.documents.map { doc ->
                    Session(
                        id = doc.id,
                        mood = doc.getString("mood") ?: "",
                        note = doc.getString("note") ?: "",
                        timestamp = doc.getLong("timestamp") ?: 0L
                    )
                }
                onResult(Result.success(sessions))
            } catch (e: Exception) {
                onResult(Result.failure(e))
            }
        }
    }

    // Data class for a session
    data class Session(
        val id: String,
        val mood: String,
        val note: String,
        val timestamp: Long
    )

    // Logout
    fun logout() {
        auth.signOut()
        isLoggedIn = false
    }
}
