package com.example.trackr

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.trackr.model.Transaction
import com.example.trackr.util.SharedPrefManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class EditTransactionActivity : AppCompatActivity() {
    private lateinit var prefManager: SharedPrefManager
    private var transactionId: Int = -1
    private lateinit var transactionList: MutableList<Transaction>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_transaction)

        prefManager = SharedPrefManager(this)
        transactionList = prefManager.getTransactions().toMutableList()

        val titleInput = findViewById<EditText>(R.id.etEditTitle)
        val amountInput = findViewById<EditText>(R.id.etEditAmount)
        val categorySpinner = findViewById<Spinner>(R.id.spinnerEditCategory)
        val dateText = findViewById<TextView>(R.id.tvEditSelectedDate)
        val btnPickDate = findViewById<Button>(R.id.btnEditSelectDate)
        val btnUpdate = findViewById<Button>(R.id.btnUpdateTransaction)



        val categories = arrayOf("Food", "Transport", "Bills", "Shopping", "Others")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = adapter

        val transactionJson = intent.getStringExtra("transaction")
        val transaction = Gson().fromJson(transactionJson, Transaction::class.java)
        transactionId = transaction.id

        titleInput.setText(transaction.title)
        amountInput.setText(transaction.amount.toString())
        dateText.text = transaction.date
        val catPosition = categories.indexOf(transaction.category)
        categorySpinner.setSelection(if (catPosition != -1) catPosition else 0)

        btnPickDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePicker = DatePickerDialog(this,
                { _, year, month, day ->
                    val selected = "$day/${month + 1}/$year"
                    dateText.text = selected
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.show()
        }

        btnUpdate.setOnClickListener {
            val updatedTitle = titleInput.text.toString()
            val updatedAmount = amountInput.text.toString().toDoubleOrNull() ?: 0.0
            val updatedCategory = categorySpinner.selectedItem.toString()
            val updatedDate = dateText.text.toString()

            val index = transactionList.indexOfFirst { it.id == transactionId }
            if (index != -1) {
                transactionList[index] = Transaction(
                    transactionId,
                    updatedTitle,
                    updatedAmount,
                    updatedCategory,
                    updatedDate
                )

                // Save the updated list
                prefManager.saveTransactions(Gson().toJson(transactionList))

                // Create an intent with the updated data
                val resultIntent = Intent().apply {
                    putExtra("updated_transaction", Gson().toJson(transactionList[index]))
                }

                // Set result and finish
                setResult(RESULT_OK, resultIntent)
                finish()
            }
        }


    }
}
