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

    fun get(key: String): String {
        val userString: String? = prefs.getString(KEY, "empty")
        var userDataVariable: String? = null

        if (userString != "empty") {
            val user: UserData = Gson().fromJson(userString, UserData::class.java)
            when (key) {
                "social" -> userDataVariable = user.social
                "token" -> userDataVariable = user.token
                "nickname" -> userDataVariable = user.nickname
                "email" -> userDataVariable = user.email
                "gender" -> userDataVariable = user.gender
                "age" -> userDataVariable = user.age
                "birthday" -> userDataVariable = user.birthday
            }
        }

        if (userDataVariable == null) {
            userDataVariable = "emptyParameter"
        }

        return userDataVariable
    }

    fun getImages(key: String): ArrayList<String>? {
        val userString: String? = prefs.getString(KEY, "empty")
        var userDataVariable: ArrayList<String> ?= ArrayList()

        val user: UserData = Gson().fromJson(userString, UserData::class.java)
        when (key) {
            "thumbnails" -> userDataVariable = user.thumbnails
            "images" -> userDataVariable = user.images
        }

        return userDataVariable
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

