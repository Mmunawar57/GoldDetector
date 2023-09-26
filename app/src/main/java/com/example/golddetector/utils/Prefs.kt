package com.example.golddetector.utils

import android.content.Context
import android.content.SharedPreferences

class Prefs(context: Context) {

    private val preferenceName = "APP_PREFS"
    private val preference: SharedPreferences =
        context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE)



    fun putBoolean(key: String, value: Boolean) {
        preference.edit().putBoolean(key, value).apply()
    }

    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean {
        return preference.getBoolean(key, defaultValue)
    }

    fun putString(key: String, value: String) {
        preference.edit().putString(key, value).apply()
    }

    fun getString(key: String, defaultValue: String = ""): String? {
        return preference.getString(key, defaultValue)
    }

    fun putInt(key: String, value: Int) {
        preference.edit().putInt(key, value).apply()
    }

    fun getInt(key: String, value: Int = 0): Int {
        return preference.getInt(key, value)
    }
}