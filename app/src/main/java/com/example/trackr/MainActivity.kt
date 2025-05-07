package com.example.trackr

import TransactionAdapter
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.trackr.databinding.ActivityMainBinding
import com.example.trackr.model.Transaction
import com.example.trackr.util.NotificationHelper
import com.example.trackr.util.SharedPrefManager
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: TransactionAdapter
    private lateinit var prefManager: SharedPrefManager
    private var transactionList = mutableListOf<Transaction>()

    private val editTransactionLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            // Get the updated transaction from the result
            val updatedJson = result.data?.getStringExtra("updated_transaction")
            updatedJson?.let {
                val updatedTransaction = Gson().fromJson(it, Transaction::class.java)

                // Find and update the transaction in the list
                val index = transactionList.indexOfFirst { it.id == updatedTransaction.id }
                if (index != -1) {
                    transactionList[index] = updatedTransaction
                    saveTransactions()

                    // Update the RecyclerView with smooth animation
                    adapter.updateList(transactionList)
                    binding.rvTransactions.smoothScrollToPosition(index)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val today = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
        binding.tvSelectedDate.text = today

        prefManager = SharedPrefManager(this)
        loadTransactions()

        val tvSelectedDate = binding.tvSelectedDate
        val calendar = Calendar.getInstance()

        tvSelectedDate.setOnClickListener {
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(this, { _, y, m, d ->
                val selectedDate = "${d}-${m + 1}-$y"
                tvSelectedDate.text = selectedDate
            }, year, month, day)

            datePicker.show()
        }

        adapter = TransactionAdapter(transactionList,
            onDeleteClick = { transaction ->
                AlertDialog.Builder(this)
                    .setTitle("Confirm Delete")
                    .setMessage("Delete this transaction?")
                    .setPositiveButton("Delete") { _, _ ->
                        // Get current position
                        val position = transactionList.indexOfFirst { it.id == transaction.id }

                        if (position != -1) {
                            // Remove from both lists
                            transactionList.removeAt(position)

                            // Save to SharedPreferences
                            prefManager.saveTransactions(transactionList)

                            // Properly notify adapter
                            adapter.notifyItemRemoved(position)

                            // Update remaining items if needed
                            if (position < transactionList.size) {
                                adapter.notifyItemRangeChanged(position, transactionList.size - position)
                            }

                            Toast.makeText(this, "Transaction deleted", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .setNegativeButton("Cancel", null)
                    .show()
            },
            onEditClick = { transaction ->
                val intent = Intent(this, EditTransactionActivity::class.java).apply {
                    putExtra("transaction", Gson().toJson(transaction))
                }
                editTransactionLauncher.launch(intent)
            }
        )

        binding.rvTransactions.layoutManager = LinearLayoutManager(this)
        binding.rvTransactions.adapter = adapter

        binding.btnAddTransaction.setOnClickListener {
            val title = binding.etTitle.text.toString()
            val amount = binding.etAmount.text.toString().toDoubleOrNull()
            val category = binding.spCategory.selectedItem.toString()

            if (title.isNotEmpty() && amount != null) {
                val date = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
                val id = transactionList.size + 1
                val transaction = Transaction(id, title, amount, category, date)
                transactionList.add(transaction)
                saveTransactions()
                adapter.updateList(transactionList)
                clearInputs()
                Toast.makeText(
                    this,
                    "Transaction added successfully!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun clearInputs() {
        binding.etTitle.text.clear()
        binding.etAmount.text.clear()
        binding.spCategory.setSelection(0)
    }

    private fun saveTransactions() {
        val json = Gson().toJson(transactionList)
        prefManager.saveTransactions(json)

        val budget = prefManager.getBudget()
        val totalSpent = transactionList.filter { it.amount > 0 }.sumOf { it.amount }

        if (totalSpent >= budget) {
            NotificationHelper.showBudgetNotification(this, "You've reached your monthly budget!")
        } else if (totalSpent >= 0.8 * budget) {
            NotificationHelper.showBudgetNotification(this, "You're nearing your monthly budget!")
        }
    }

    private fun loadTransactions() {
        transactionList = prefManager.getTransactions().toMutableList()
    }




}
