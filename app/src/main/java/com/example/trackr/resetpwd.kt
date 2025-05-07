package com.example.trackr

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class resetpwd : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_resetpwd)

        // Find the  button
        val resetpwdButton = findViewById<Button>(R.id.resetpwdbtn)

        // Set an OnClickListener for the button
        resetpwdButton.setOnClickListener {
            // Create an Intent to navigate to the Onboarding1 activity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // Find the  button
        val cancelButton = findViewById<Button>(R.id.cancelbtn)

        // Set an OnClickListener for the button
        cancelButton.setOnClickListener {
            // Create an Intent to navigate to the Onboarding1 activity
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}