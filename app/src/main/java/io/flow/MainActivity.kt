package io.flow

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.util.UserSharedPreferences

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val userSharedPreferences = UserSharedPreferences(this)
        userSharedPreferences.clear()

        val user = userSharedPreferences.get()
        val startActivity =
            if (user == null) LoginActivity::class.java else PickActivity::class.java
        val intent = Intent(this, startActivity)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        return startActivity(intent)
    }
}
