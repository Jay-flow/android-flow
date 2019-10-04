package io.db

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.provider.Telephony.Mms.Part.FILENAME
import android.util.Log
import com.google.gson.Gson
import io.data.UserData


class UserSharedPreferences(context: Context) {
    private val FILE_NAME= "userInfo"
    private val KEY = "user"
    private val prefs: SharedPreferences = context.getSharedPreferences(FILE_NAME, 0)

    fun get(key: String): String {
        val userString: String? = prefs.getString(KEY, "empty")

        var userDataVariable = "emptyParameter"
        if(userString != "empty") {
            val user: UserData = Gson().fromJson(userString, UserData::class.java)
            when (key) {
                "social" -> userDataVariable = user.social
                "token" -> userDataVariable = user.token as String
                "name" -> userDataVariable = user.name as String
                "email" -> userDataVariable = user.email as String
                "gender" -> userDataVariable = user.gender as String
                "age" -> userDataVariable = user.age as String
                "birthday" -> userDataVariable = user.birthday as String
                "profile_thum" -> userDataVariable = user.profile_thum as String
                "profile_image" -> userDataVariable = user.profile_image as String
            }
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
}