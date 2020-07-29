package com.example.androidkotlintemplate.Util

import android.content.Context
import android.content.SharedPreferences
import java.util.*

class PreferencesHelper(context: Context) {
    val SHARED_PREFERENCES_NAME = "app_pref"
    private var sharedPreferences: SharedPreferences

    init {
        sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    companion object {
        lateinit var preferencesHelper: PreferencesHelper

        @JvmStatic
        fun create(context: Context) {
            preferencesHelper = PreferencesHelper(context)
        }

        @JvmStatic
        fun get(): PreferencesHelper {
            return preferencesHelper
        }
    }

    fun putInt(key: String?, value: Int) {
        sharedPreferences.edit().putInt(key, value).apply()
    }

    fun putLong(key: String?, value: Long) {
        sharedPreferences.edit().putLong(key, value).apply()
    }

    fun putFloat(key: String?, value: Float) {
        sharedPreferences.edit().putFloat(key, value).apply()
    }

    fun putString(key: String?, value: String?) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    fun putBoolean(key: String?, value: Boolean) {
        sharedPreferences.edit().putBoolean(key, value).apply()
    }

    fun getInt(key: String?, defaultValue: Int): Int {
        return sharedPreferences.getInt(key, defaultValue)
    }

    fun getLong(key: String?, defaultValue: Long): Long {
        return sharedPreferences.getLong(key, defaultValue)
    }

    fun getFloat(key: String?, defaultValue: Float): Float {
        return sharedPreferences.getFloat(key, defaultValue)
    }

    fun getString(key: String?, defaultValue: String?): String {
        return sharedPreferences.getString(key,defaultValue).toString()
    }

    fun getBoolean(key: String?, defaultValue: Boolean): Boolean {
        return sharedPreferences.getBoolean(key, defaultValue)
    }

    fun getStringSet(key: String?): Set<String?>? {
        return sharedPreferences.getStringSet(key, HashSet())
    }

    fun putStringSet(key: String?, value: Set<String?>?) {
        sharedPreferences.edit().putStringSet(key, value).apply()
    }

}