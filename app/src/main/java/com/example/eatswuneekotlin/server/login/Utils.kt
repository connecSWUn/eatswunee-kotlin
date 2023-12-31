package com.example.eatswuneekotlin.server.login

import android.content.Context
import android.content.SharedPreferences

class Utils(private val mContext: Context) {
    init {
        prefs = mContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        prefsEditor = prefs.edit()
    }

    companion object {
        private const val PREFS = "prefs"
        private const val Access_Token = "Access_Token"
        private const val Refresh_Token = "Refresh_Token"
        private lateinit var prefs: SharedPreferences
        private lateinit var prefsEditor: SharedPreferences.Editor
        private var instance: Utils? = null

        @Synchronized
        fun init(context: Context): Utils? {
            if (instance == null) instance = Utils(context)
            return instance
        }

        fun setAccessToken(value: String?) {
            prefsEditor.putString(Access_Token, value).commit()
        }

        fun getAccessToken(defValue: String?): String? {
            return prefs.getString(Access_Token, defValue)
        }

        fun setRefreshtoken(value: String?) {
            prefsEditor.putString(Refresh_Token, value).commit()
        }

        fun getRefreshToken(defValue: String?): String? {
            return prefs.getString(Refresh_Token, defValue)
        }

        fun clearToken() {
            prefsEditor.clear().apply()
        }
    }
}