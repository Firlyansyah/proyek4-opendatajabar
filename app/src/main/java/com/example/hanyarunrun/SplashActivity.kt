package com.example.hanyarunrun

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreferences: SharedPreferences = getSharedPreferences("onboarding", MODE_PRIVATE)
        val isFirstTime = sharedPreferences.getBoolean("isFirstTime", true)

        val nextActivity = if (isFirstTime) {
            OnboardingActivity::class.java
        } else {
            MainActivity::class.java
        }

        startActivity(Intent(this, nextActivity))
        finish() // Tutup SplashActivity
    }
}
