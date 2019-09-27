package io.db

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import io.data.UserData

private const val FILENAME = "prefs"
private const val PREF_TEXT = "Text"

class UserSharedPreferences(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(FILENAME, 0)

    fun get(value: String) = prefs.getString(value, null)!!
    fun getIsLogin(): Boolean = prefs.getBoolean("isLogin", false)

    fun set(user: UserData) {
        prefs.edit().putBoolean("isLogin", true).apply()
        prefs.edit().putString("social", user.social).apply()
        prefs.edit().putString("token", user.token).apply()
        prefs.edit().putString("name", user.name).apply()
        prefs.edit().putString("email", user.email).apply()
        prefs.edit().putString("gender", user.gender).apply()
        prefs.edit().putString("age", user.age).apply()
        prefs.edit().putString("birthday", user.birthday).apply()
        prefs.edit().putString("profile_thum", user.profile_thum).apply()
        prefs.edit().putString("profile_image", user.profile_image).apply()
    }
}