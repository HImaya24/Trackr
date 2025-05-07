

package com.example.trackr

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.trackr.util.SharedPrefManager
//import com.example.yourapp.SettingsActivity
import java.text.SimpleDateFormat
import java.util.*

class HomeActivity : AppCompatActivity() {
    // Declare TextView variables
    private lateinit var tvBudget: TextView
    private lateinit var tvSpent: TextView
    private lateinit var prefManager: SharedPrefManager
    private lateinit var tvBalance: TextView


    @SuppressLint("SetTextI18n")
    fun updateUI() {
        val budget = prefManager.getBudget()
        val spent = getMonthlyTotal()
        val balance = budget - spent

        tvBudget.text = "$${"%.2f".format(budget)}"
        tvSpent.text = "$${"%.2f".format(spent)}"
        tvBalance.text = "$${"%.2f".format(balance)}"
    }

    private fun getMonthlyTotal(): Double {
        val transactions = prefManager.getTransactions()
        if (transactions.isEmpty()) return 0.0

        val currentMonth = SimpleDateFormat("MM", Locale.getDefault()).format(Date())
        val currentYear = SimpleDateFormat("yyyy", Locale.getDefault()).format(Date())

        // Try multiple possible date formats
        val possibleDateFormats = arrayOf(
            "d/M/yyyy",  // e.g. "10/4/2025"
            "dd/MM/yyyy", // e.g. "10/04/2025"
            "d-M-yyyy",   // e.g. "10-4-2025"
            "dd-MM-yyyy", // e.g. "10-04-2025"
            "M/d/yyyy",   // e.g. "4/10/2025" (month/day)
            "MM/dd/yyyy"  // e.g. "04/10/2025"
        )

        return transactions.filter { transaction ->
            var parsedDate: Date? = null
            for (format in possibleDateFormats) {
                try {
                    parsedDate = SimpleDateFormat(format, Locale.getDefault()).parse(transaction.date)
                    if (parsedDate != null) break
                } catch (e: Exception) {

                }
            }

            if (parsedDate == null) {
                // If we can't parse the date, skip this transaction
                false
            } else {
                val transactionMonth = SimpleDateFormat("MM", Locale.getDefault()).format(parsedDate)
                val transactionYear = SimpleDateFormat("yyyy", Locale.getDefault()).format(parsedDate)
                transactionMonth == currentMonth && transactionYear == currentYear
            }
        }.sumOf { it.amount }
    }

    @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Initialize SharedPrefManager
        prefManager = SharedPrefManager(this)

        // Initialize TextViews
        tvBudget = findViewById(R.id.tvBudget)
        tvSpent = findViewById(R.id.tvSpent)
        tvBalance = findViewById(R.id.tvBalance)

        // Set click listeners for buttons
        val btnAddTransaction = findViewById<Button>(R.id.btnAddTransaction)
        val btnViewBudget = findViewById<Button>(R.id.btnViewBudget)
        val btnCategoryAnalysis = findViewById<Button>(R.id.btnCategoryAnalysis)
        val btnBackup = findViewById<Button>(R.id.btnBackup)

        btnAddTransaction.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
        btnViewBudget.setOnClickListener {
            startActivity(Intent(this, BudgetSetupActivity::class.java))
        }
        btnCategoryAnalysis.setOnClickListener {
            startActivity(Intent(this, CategoryAnalysisActivity::class.java))
        }
        btnBackup.setOnClickListener {
            startActivity(Intent(this, BackupRestoreActivity::class.java))
        }

        // Load data from SharedPreferences
        updateUI()
    }
    override fun onResume() {
        super.onResume()
        updateUI() // Forces refresh when returning to HomeActivity
    }
}
