package io.flow

import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.kakao.auth.AuthType
import com.kakao.auth.ISessionCallback
import com.kakao.auth.Session
import com.kakao.network.ErrorResult
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.MeV2ResponseCallback
import com.kakao.usermgmt.response.MeV2Response
import com.kakao.util.exception.KakaoException
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private var callbackManager: CallbackManager? = null
    private lateinit var kakaoCallback: SessionStatusCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        facebookLogin()
        kakaoLogin()
    }

    private fun kakaoLogin() {
        kakaoCallback = SessionStatusCallback()
        Session.getCurrentSession().addCallback(kakaoCallback)
        Session.getCurrentSession().checkAndImplicitOpen()

        btnLoginKakao.setOnClickListener {
            Handler().postDelayed({
                Session.getCurrentSession().open(AuthType.KAKAO_TALK, this)
            }, 100)
        }
    }

    private fun facebookLogin() {
        btnLoginFacebook.setOnClickListener {
            // Login
            callbackManager = CallbackManager.Factory.create()
            LoginManager.getInstance()
                .logInWithReadPermissions(this, Arrays.asList("public_profile", "email"))
            LoginManager.getInstance().registerCallback(callbackManager,
                object : FacebookCallback<LoginResult> {
                    override fun onSuccess(loginResult: LoginResult) {
                        Log.d("MainActivity", "Facebook token: " + loginResult.accessToken.token)
                        //startActivity(Intent(applicationContext, AuthenticatedActivity::class.java))
                    }
                    override fun onCancel() {
                        Log.d("MainActivity", "Facebook onCancel.")

                    }
                    override fun onError(error: FacebookException) {
                        Log.d("MainActivity", "Facebook onError.")
                    }
                })
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return
        }

        //super.onActivityResult(requestCode, resultCode, data);
        callbackManager?.onActivityResult(requestCode, resultCode, data)
    }

    fun successLogin() {
        Toast.makeText(this, "카카오로그인", Toast.LENGTH_SHORT).show()
    }

    private fun kakaoRequestMe() {
        UserManagement.getInstance().me(object : MeV2ResponseCallback() {
            override fun onSessionClosed(errorResult: ErrorResult?) {
                Log.d("kakaoError", errorResult.toString())
            }

            override fun onSuccess(result: MeV2Response?) {
                successLogin()
            }
        })
    }

    inner class SessionStatusCallback : ISessionCallback {
        override fun onSessionOpenFailed(exception: KakaoException?) {
            Log.d("kakaoError", exception.toString())
        }

        override fun onSessionOpened() {
            kakaoRequestMe()
        }
    }
}
