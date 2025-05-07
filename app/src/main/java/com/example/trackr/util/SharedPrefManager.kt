

package com.example.trackr.util

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.trackr.model.Transaction
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class SharedPrefManager(context: Context) {
    private var preferences: SharedPreferences = context.getSharedPreferences("trackr_prefs", Context.MODE_PRIVATE)
    private var editor: SharedPreferences.Editor = preferences.edit()
    private val gson = Gson()

    companion object {
        private const val DEFAULT_BUDGET = 1200.0
        private const val DEFAULT_CURRENCY = "$"
        private const val KEY_BUDGET = "budget"
        private const val KEY_TRANSACTIONS = "transactions"
        private const val KEY_CURRENCY = "currency"
        private const val KEY_NOTIFICATIONS_ENABLED = "notifications_enabled"
        private const val PREFS_NAME = "TrackrPrefs"

        private fun isValidTransactionJson(json: String?): Boolean {
            return json != null && json.trim().startsWith("[") && json.trim().endsWith("]")
        }
    }

    init {
        preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        editor = preferences.edit()
    }
    // Save transactions as proper JSON string
    fun saveTransactions(transactions: List<Transaction>) {
        val json = gson.toJson(transactions)
        editor.putString(KEY_TRANSACTIONS, json)
        editor.apply()
    }

    // Robust transaction retrieval
    fun getTransactions(): List<Transaction> {
        val json = preferences.getString(KEY_TRANSACTIONS, null) ?: return emptyList()

        return try {
            if (isValidTransactionJson(json)) {
                val type = object : TypeToken<List<Transaction>>() {}.type
                gson.fromJson(json, type) ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("SharedPrefManager", "Failed to parse transactions", e)
            emptyList()
        }
    }

    // For direct JSON backup/restore
    fun getTransactionsJson(): String {
        return preferences.getString(KEY_TRANSACTIONS, "[]") ?: "[]"
    }

    fun saveTransactionsFromJson(json: String) {
        if (isValidTransactionJson(json)) {
            editor.putString(KEY_TRANSACTIONS, json)
            editor.apply()
        } else {
            throw IllegalArgumentException("Invalid transactions JSON format")
        }
    }

    fun saveBudget(budget: Double) {
        editor.putFloat(KEY_BUDGET, budget.toFloat())
        editor.apply()
    }

    fun getBudget(): Double {
        return preferences.getFloat(KEY_BUDGET, DEFAULT_BUDGET.toFloat()).toDouble()
    }

    fun saveTransactions(json: String) {
        editor.putString(KEY_TRANSACTIONS, json)
        editor.apply()
    }



    fun saveCurrency(currency: String) {
        editor.putString(KEY_CURRENCY, currency)
        editor.apply()
    }

    fun getCurrency(): String {
        return preferences.getString(KEY_CURRENCY, DEFAULT_CURRENCY) ?: DEFAULT_CURRENCY
    }

    fun setNotificationsEnabled(enabled: Boolean) {
        editor.putBoolean(KEY_NOTIFICATIONS_ENABLED, enabled)
        editor.apply()
    }

    fun isNotificationsEnabled(): Boolean {
        return preferences.getBoolean(KEY_NOTIFICATIONS_ENABLED, true) // Default is true
    }
}