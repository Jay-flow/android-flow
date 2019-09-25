package io.common

import android.util.Log
import io.data.UserData

class User {
    fun save(user: UserData) {
        Log.d("save", user.toString())
    }
}

