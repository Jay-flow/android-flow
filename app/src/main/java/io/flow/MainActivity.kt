package io.flow

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.db.UserSharedPreferences

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val isLogin: Boolean = UserSharedPreferences(this).getIsLogin()
        if(isLogin) {
            intent = Intent(this, PickActivity::class.java)
        } else {
            intent = Intent(this, LoginActivity::class.java)
        }
        return startActivity(intent)
    }
}
