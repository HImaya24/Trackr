package com.example.trackr

import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.trackr.model.Transaction

object TransactionManager {
    val transactions = mutableListOf<Transaction>()



    fun getMonthlyTotal(): Double {
        return transactions.sumOf { it.amount }
    }

    fun getCategorySummary(): Map<String, Double> {
        return transactions.groupBy { it.category }
            .mapValues { (_, transactions) -> transactions.sumOf { it.amount } }
    }

    // Add this to filter by current month
    @RequiresApi(Build.VERSION_CODES.O)
    private fun getCurrentMonthTransactions(): List<Transaction> {
        val currentMonth = java.time.LocalDate.now().monthValue
        return transactions.filter {
            java.time.LocalDate.parse(it.date).monthValue == currentMonth
        }
    }


}
