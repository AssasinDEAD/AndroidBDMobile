package com.example.mobilebd

import android.content.Context
import android.content.SharedPreferences

class UserPreferences(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    fun saveUser(username: String, email: String, password: String) {
        val editor = sharedPreferences.edit()
        editor.putString("username", username)
        editor.putString("email", email)
        editor.putString("password", password)
        editor.putBoolean("isRegistered", true)
        editor.apply()
    }

    fun getUserCredentials(): Pair<String?, String?> {
        val email = sharedPreferences.getString("email", null)
        val password = sharedPreferences.getString("password", null)
        return Pair(email, password)
    }

    fun getUsername(): String? {
        return sharedPreferences.getString("username", "User")
    }

    fun isUserRegistered(): Boolean {
        return sharedPreferences.getBoolean("isRegistered", false)
    }

    fun clearUserData() {
        sharedPreferences.edit().clear().apply()
    }
}
