package com.example.hanyarunrun

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2

class OnboardingActivity : AppCompatActivity() {
    private lateinit var viewPager: ViewPager2
    private lateinit var btnNext: Button
    private lateinit var sharedPreferences: SharedPreferences

    private val texts = listOf(
        "Selamat datang di Aplikasi Data Kebakaran Jawa Barat!",
        "Terintegrasi dengan API Open Data Jabar!",
        "Anda dapat membuat dan mengedit data anda sendiri!",
        "Ayo mulai perjalanan anda!"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences = getSharedPreferences("onboarding", MODE_PRIVATE)
        val isFirstTime = sharedPreferences.getBoolean("isFirstTime", true)

        if (!isFirstTime) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }

        setContentView(R.layout.activity_onboarding)

        viewPager = findViewById(R.id.viewPager)
        btnNext = findViewById(R.id.btn_next)

        val adapter = OnboardingAdapter(texts)
        viewPager.adapter = adapter

        btnNext.setOnClickListener {
            if (viewPager.currentItem < texts.size - 1) {
                viewPager.currentItem += 1
            } else {
                // Simpan status bahwa onboarding sudah dilihat
                sharedPreferences.edit().putBoolean("isFirstTime", false).apply()

                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                btnNext.text = if (position == texts.size - 1) "Mulai" else "Selanjutnya"
            }
        })
    }
}
