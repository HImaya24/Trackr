package com.example.trackr

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.trackr.model.Transaction
import com.example.trackr.util.FileManager
import com.example.trackr.util.SharedPrefManager

class BackupRestoreActivity : AppCompatActivity() {

    private lateinit var backupBtn: Button
    private lateinit var restoreBtn: Button
    private lateinit var prefManager: SharedPrefManager

    @SuppressLint("UseSwitchCompatOrMaterialCode", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_backup_restore)

        // In your onCreate or onViewCreated
        val notificationSwitch = findViewById<Switch>(R.id.notificationSwitch)
        val sharedPrefManager = SharedPrefManager(this)


// Set the initial state from saved preferences
        notificationSwitch.isChecked = sharedPrefManager.isNotificationsEnabled()

// Save the state when changed
        notificationSwitch.setOnCheckedChangeListener { _, isChecked ->
            sharedPrefManager.setNotificationsEnabled(isChecked)
        }

        prefManager = SharedPrefManager(this)
        backupBtn = findViewById(R.id.backupBtn)
        restoreBtn = findViewById(R.id.restoreBtn)

        backupBtn.setOnClickListener {
            try {
                val json = prefManager.getTransactionsJson()
                if (FileManager.backupData(this, json)) {
                    Toast.makeText(this, "Backup successful!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Backup failed!", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this, "Backup error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }

        restoreBtn.setOnClickListener {
            try {
                val restoredJson = FileManager.restoreData(this)
                if (restoredJson != null) {
                    prefManager.saveTransactionsFromJson(restoredJson)
                    Toast.makeText(this, "Restored successfully", Toast.LENGTH_SHORT).show()
                    finish() // Properly return to previous activity
                }
            } catch (e: IllegalArgumentException) {
                Toast.makeText(this, "Invalid backup file", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(this, "Restore failed", Toast.LENGTH_SHORT).show()
            }
        }
    }
}