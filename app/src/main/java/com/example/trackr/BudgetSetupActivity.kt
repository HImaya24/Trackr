

package com.example.trackr

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.trackr.util.SharedPrefManager

class BudgetSetupActivity : AppCompatActivity() {
    private lateinit var budgetInput: EditText
    private lateinit var saveBtn: Button
    private lateinit var viewBudgetBtn: Button
    private lateinit var prefManager: SharedPrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_budget_setup)

        // Initialize SharedPrefManager
        prefManager = SharedPrefManager(this)
        val backhome = findViewById<TextView>(R.id.backhometext)

        backhome.setOnClickListener {
            try {
                startActivity(Intent(this, HomeActivity::class.java).apply {
                    // Optional flags
                    flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                })
            } catch (e: Exception) {
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }

        budgetInput = findViewById(R.id.budgetInput)
        saveBtn = findViewById(R.id.saveBudgetBtn)
        viewBudgetBtn = findViewById(R.id.viewBudgetBtn)

        // Pre-fill the current budget value if available
        val currentBudget = prefManager.getBudget()
        if (currentBudget > 0) {
            budgetInput.setText(currentBudget.toString())
        }

        saveBtn.setOnClickListener {
            val budgetStr = budgetInput.text.toString()
            if (budgetStr.isNotEmpty()) {
                try {
                    val budget = budgetStr.toDouble()
                    prefManager.saveBudget(budget)
                    Toast.makeText(this, "Budget Saved!", Toast.LENGTH_SHORT).show()

                    // Return to HomeActivity to see updated budget
                    val intent = Intent(this, HomeActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP  // Clear activity stack
                    startActivity(intent)
                    finish()
                } catch (e: NumberFormatException) {
                    Toast.makeText(this, "Please enter a valid number", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please enter a budget", Toast.LENGTH_SHORT).show()
            }
        }

        viewBudgetBtn.setOnClickListener {
            val intent = Intent(this, ViewBudgetActivity::class.java)
            startActivity(intent)
        }
    }
}