package io.db

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.provider.Telephony.Mms.Part.FILENAME
import android.util.Log
import com.google.gson.Gson
import io.data.UserData


class UserSharedPreferences(context: Context) {
    private val FILE_NAME = "userInfo"
    private val KEY = "user"
    private val prefs: SharedPreferences = context.getSharedPreferences(FILE_NAME, 0)

    fun get(key: String, index: Int? = null): String {
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

            if (index != null) {
                when (key) {
                    "thumbnails" -> userDataVariable = user.thumbnails?.get(index)
                    "images" -> userDataVariable = user.images?.get(index)
                }
            }
        }

        if (userDataVariable == null) {
            userDataVariable = "emptyParameter"
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