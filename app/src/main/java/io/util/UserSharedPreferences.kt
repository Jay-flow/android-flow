package io.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import io.data.UserData


class UserSharedPreferences(context: Context) {
    private val FILE_NAME = "userInfo"
    private val KEY = "user"
    private val prefs: SharedPreferences = context.getSharedPreferences(FILE_NAME, 0)

    fun get(): UserData? {
        val prefsUserData: String? = prefs.getString(KEY, "empty")
        var user: UserData? = null

        if(prefsUserData != "empty") {
            user = Gson().fromJson(prefsUserData, UserData::class.java)
        }
        return user
    }

    @SuppressLint("CommitPrefEdits")
    fun set(user: UserData) {
        val userInfo: String = Gson().toJson(user)
        val editor: SharedPreferences.Editor = prefs.edit()
        editor.putString(KEY, userInfo)
        editor.apply()
    }

    fun clear() {
        val editor: SharedPreferences.Editor = prefs.edit()
        editor.clear()
        editor.apply()
    }
}

