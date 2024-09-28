package com.example.test2.presentation.n.activities

import android.app.Application
import android.util.Log
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        val firebaseApp = FirebaseApp.initializeApp(this)
        if (firebaseApp != null) {
            Log.d("MyApplication", "Firebase initialized successfully")
        } else {
            Log.e("MyApplication", "Firebase initialization failed")
        }
    }
}
