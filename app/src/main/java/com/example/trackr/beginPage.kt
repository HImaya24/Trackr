package com.example.trackr

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class beginPage : AppCompatActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_begin_page)

            val logo = findViewById<ImageView>(R.id.logo)

            logo.setOnClickListener {
                val intent = Intent(this, onboardingActivity::class.java)
                startActivity(intent)
                finish()
            }
        }


}