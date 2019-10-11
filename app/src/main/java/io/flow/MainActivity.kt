package io.flow

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.util.UserSharedPreferences

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val userSharedPreferences = UserSharedPreferences(this)
        //userSharedPreferences.clear()

        val email = userSharedPreferences.get("email")
        val startActivity = if (email == "emptyParameter") LoginActivity::class.java else PickActivity::class.java
        val intent = Intent(this, startActivity)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        return startActivity(intent)
    }
}
