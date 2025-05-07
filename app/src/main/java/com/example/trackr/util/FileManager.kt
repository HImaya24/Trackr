package com.example.trackr.util

import android.content.Context
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser

object FileManager {
    fun backupData(context: Context, json: String): Boolean {
        return try {
            // Convert to pretty-printed JSON
            val prettyJson = GsonBuilder()
                .setPrettyPrinting()
                .create()
                .toJson(JsonParser.parseString(json))

            context.openFileOutput("trackr_backup.json", Context.MODE_PRIVATE).use {
                it.write(prettyJson.toByteArray()) //in formatted text
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    fun restoreData(context: Context): String? {
        return try {
            context.openFileInput("trackr_backup.json").bufferedReader().use {
                it.readText()
            }
        } catch (e: Exception) {
            null
        }
    }
}