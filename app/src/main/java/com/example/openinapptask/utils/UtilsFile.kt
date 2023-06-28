package com.example.openinapptask.utils

import android.app.Application
import android.content.Context
import android.content.SharedPreferences

class UtilsFile : Application() {
    companion object {
        private const val TOKEN_KEY = "TOKEN_KEY"
        private const val PREFERENCES_NAME = "MyPreferences"

        // Function to save the token
        fun saveToken(context: Context, token: String) {
            val preferences: SharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
            val editor: SharedPreferences.Editor = preferences.edit()
            editor.putString(TOKEN_KEY, token)
            editor.apply()
        }

        // Function to retrieve the token
        fun getToken(context: Context): String? {
            val preferences: SharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
            return preferences.getString(TOKEN_KEY, null)
        }

    }
}