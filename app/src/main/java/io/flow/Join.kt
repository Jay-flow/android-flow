package io.flow

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import com.google.firebase.firestore.FirebaseFirestore
import io.common.User
import io.data.UserData
import kotlinx.android.synthetic.main.activity_join.*

class Join : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)

        val user = intent.getParcelableExtra<UserData>("user")

        name.setText(user.name)
        email.setText(user.email)

        Log.d("JoinUSER", user.name)
    }

    // 파이어베이스 데이터 입력하는거 구현 해야됨 !!!!!!!!
    private fun saveUserInfo(user: UserData) {
        db.collection("users").document(user.email.toString()).set(user)
            .addOnSuccessListener { documentReference ->
                User().save(user)
            }
            .addOnFailureListener { e -> }
    }

}
