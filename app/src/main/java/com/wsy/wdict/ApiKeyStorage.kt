package com.wsy.wdict

import android.content.Context
import android.content.SharedPreferences

class ApiKeyStorage(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    var apiKey: String
        get() = prefs.getString(KEY_API_KEY, "") ?: ""
        set(value) { prefs.edit().putString(KEY_API_KEY, value).apply() }

    var model: String
        get() = prefs.getString(KEY_MODEL, DEFAULT_MODEL) ?: DEFAULT_MODEL
        set(value) { prefs.edit().putString(KEY_MODEL, value).apply() }

    companion object {
        private const val PREFS_NAME = "wdict_prefs"
        private const val KEY_API_KEY = "api_key"
        private const val KEY_MODEL = "model"
        const val DEFAULT_MODEL = "openai/gpt-4o-mini"
    }
}
