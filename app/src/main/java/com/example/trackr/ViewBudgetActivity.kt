package com.example.trackr


import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.trackr.util.SharedPrefManager



class ViewBudgetActivity : AppCompatActivity() {
    private lateinit var tvBudgetDisplay: TextView
    private lateinit var prefManager: SharedPrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_budget)

        tvBudgetDisplay = findViewById(R.id.tvBudgetDisplay)
        prefManager = SharedPrefManager(this)

        updateBudgetDisplay()
    }

    private fun updateBudgetDisplay() {
        val budget = prefManager.getBudget()
        if (budget > 0) {
            tvBudgetDisplay.text = "Your Monthly Budget: Rs. ${"%.2f".format(budget)}"
        } else {
            tvBudgetDisplay.text = "Your Monthly Budget: Rs. Not Set"
        }
    }

    // Call this whenever you update the budget
    fun onBudgetSet(newBudget: Double) {
        prefManager.saveBudget(newBudget)
        updateBudgetDisplay()
    }
}