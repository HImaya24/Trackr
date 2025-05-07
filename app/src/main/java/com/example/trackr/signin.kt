package com.example.trackr

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class signin : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signin)

        val signupbtn = findViewById<Button>(R.id.signupbtn)
        val loginbtn = findViewById<Button>(R.id.loginbtn)

        signupbtn.setOnClickListener {
            val username = findViewById<EditText>(R.id.username).text.toString()
            val password = findViewById<EditText>(R.id.password).text.toString()
            val confirmPassword = findViewById<EditText>(R.id.confirmpwd).text.toString()
            val email = findViewById<EditText>(R.id.TextEmailAddress).text.toString()
            val mobileNumber = findViewById<EditText>(R.id.editTextPhone).text.toString()

            if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || email.isEmpty() || mobileNumber.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            } else if (password != confirmPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            } else {
                UserDataManager.registerUser(username, password, email, mobileNumber)
                Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show()

                // Go to Login screen after registration
                val intent = Intent(this, Login::class.java)
                startActivity(intent)
                finish()
            }
        }

        // 👇 Moved this out of signup click listener
        loginbtn.setOnClickListener {
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
