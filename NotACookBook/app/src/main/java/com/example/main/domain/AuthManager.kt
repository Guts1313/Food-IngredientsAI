package com.example.main.domain

import com.google.firebase.auth.FirebaseAuth

class AuthManager(private val auth: FirebaseAuth = FirebaseAuth.getInstance()) {

    fun isLoggedIn(): Boolean {
        return auth.currentUser != null
    }
    fun signOut() {
        auth.signOut()
    }
    fun addAuthStateListener(onAuthStateChanged: (Boolean) -> Unit) {
        val authListener = FirebaseAuth.AuthStateListener {
            val isLoggedIn = it.currentUser != null
            onAuthStateChanged(isLoggedIn)
        }
        auth.addAuthStateListener(authListener)
    }

    // Sign-in function
    fun signIn(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    val errorMessage = task.exception?.localizedMessage ?: "Login failed"
                    onError(errorMessage)
                }
            }
    }



}
