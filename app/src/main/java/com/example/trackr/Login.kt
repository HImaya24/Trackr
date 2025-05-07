package com.example.trackr

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        // Find the  button
        val loginnowButton = findViewById<Button>(R.id.loginnowbtn)

        // Set an OnClickListener for the button
        loginnowButton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        // Find the textView
        val textViewNext = findViewById<TextView>(R.id.forgotpwdtxt)

        // Set an OnClickListener for the ImageView
        textViewNext.setOnClickListener {
            // Create an Intent to navigate to the Onboarding1 activity
            val intent = Intent(this, resetpwd::class.java)
            startActivity(intent)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}