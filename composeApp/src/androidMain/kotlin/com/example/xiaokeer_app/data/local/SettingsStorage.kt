package com.example.xiaokeer_app.data.local

import android.content.Context
import android.content.SharedPreferences

class SettingsStorage(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    var serverUrl: String
        get() = prefs.getString(KEY_SERVER_URL, DEFAULT_SERVER_URL) ?: DEFAULT_SERVER_URL
        set(value) = prefs.edit().putString(KEY_SERVER_URL, value).apply()

    var headerKey: String
        get() = prefs.getString(KEY_HEADER_KEY, DEFAULT_HEADER_KEY) ?: DEFAULT_HEADER_KEY
        set(value) = prefs.edit().putString(KEY_HEADER_KEY, value).apply()

    var headerValue: String
        get() = prefs.getString(KEY_HEADER_VALUE, "") ?: ""
        set(value) = prefs.edit().putString(KEY_HEADER_VALUE, value).apply()

    companion object {
        private const val PREFS_NAME = "xiaokeer_settings"
        private const val KEY_SERVER_URL = "server_url"
        private const val KEY_HEADER_KEY = "header_key"
        private const val KEY_HEADER_VALUE = "header_value"

        private const val DEFAULT_SERVER_URL = ""
        private const val DEFAULT_HEADER_KEY = "token"
    }
}
