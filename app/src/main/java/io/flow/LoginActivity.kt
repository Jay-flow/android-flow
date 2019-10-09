package io.flow

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.kakao.network.ErrorResult
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.MeV2ResponseCallback
import com.kakao.usermgmt.response.MeV2Response
import com.kakao.util.exception.KakaoException
import kotlinx.android.synthetic.main.activity_login.*
import java.util.*
import com.facebook.*
import com.kakao.auth.*
import com.kakao.auth.authorization.accesstoken.AccessToken
import io.data.UserData


class LoginActivity : AppCompatActivity() {

    private var callbackManager: CallbackManager? = null
    private lateinit var kakaoCallback: SessionStatusCallback
    private lateinit var userData: UserData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        loginActivity.setAnimation(fadeIn)
        facebookLogin()
        kakaoLogin()
        noSocialLogin()

        //카톡 자동 로그인 방지 코드 나중에 아래 삭제 하기
//        UserManagement.getInstance().requestUnlink(object : UnLinkResponseCallback() {
//            override fun onFailure(errorResult: ErrorResult?) {}
//            override fun onSessionClosed(errorResult: ErrorResult) {}
//            override fun onNotSignedUp() {}
//            override fun onSuccess(userId: Long?) {}
//        })
    }

    private fun noSocialLogin() {
        noSocialLogin.setOnClickListener {
            userData = UserData(
                "N",
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
            )
            nextActivity(userData)
        }
    }

    private fun kakaoLogin() {
        kakaoCallback = SessionStatusCallback()
        Session.getCurrentSession().addCallback(kakaoCallback)
        Session.getCurrentSession().checkAndImplicitOpen()

        btnLoginKakao.setOnClickListener {
            buttonClickAnimation(btnLoginKakao)
            Handler().postDelayed({
                Session.getCurrentSession().open(AuthType.KAKAO_TALK, this)
            }, 100)
        }
    }

    private fun facebookLogin() {
        btnLoginFacebook.setOnClickListener {
            buttonClickAnimation(btnLoginFacebook)
            // Login
            callbackManager = CallbackManager.Factory.create()
            LoginManager.getInstance()
                .logInWithReadPermissions(this, Arrays.asList("public_profile", "email"))
            LoginManager.getInstance().registerCallback(callbackManager,
                object : FacebookCallback<LoginResult> {
                    override fun onSuccess(loginResult: LoginResult) {
                        val request = GraphRequest.newMeRequest(
                            loginResult.accessToken
                        ) { _, response ->
                            Log.d("FacebookUser", response.toString())

                            val social = "F"
                            val token: String = loginResult.accessToken.token.toString()
                            val name: String? = response.jsonObject.get("name").toString()
                            val email: String = response.jsonObject.get("email").toString()
                            var profile_image =
                                response.jsonObject.getJSONObject("picture").getJSONObject("data")
                                    .get("url").toString()
                            userData = UserData(
                                social,
                                token,
                                name,
                                email,
                                null,
                                null,
                                null,
                                null,
                                profile_image
                            )
                            nextActivity(userData)
                        }

                        val parameters = Bundle()
                        parameters.putString(
                            "fields",
                            "name,email,gender,birthday,location,picture.type(large),age_range"
                        )
                        request.parameters = parameters
                        request.executeAsync()
                    }

                    override fun onCancel() {
                        Log.d("LoginActivity", "Facebook onCancel.")

                    }

                    override fun onError(error: FacebookException) {
                        Log.d("LoginActivity", "Facebook onError.")
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

    private fun kakaoRequestMe() {
        UserManagement.getInstance().me(object : MeV2ResponseCallback() {
            override fun onSessionClosed(errorResult: ErrorResult?) {
                Log.d("kakaoError", errorResult.toString())
            }

            override fun onSuccess(result: MeV2Response?) {
                Log.d("kakaoLogin", result.toString())
                val social = "K"
                val token: String = AccessToken.Factory.getInstance().accessToken.toString()
                val name: String? = result?.properties?.get("nickname")
                val email: String = result!!.getKakaoAccount().email
                val profile_thum: String? = result?.properties?.get("thumbnail_image")
                val gender: String? =
                    result!!.getKakaoAccount().gender.toString().toLowerCase(Locale.US)
                val age: String? = result!!.getKakaoAccount().ageRange.toString().substring(4, 6)
                val birthday: String? = result!!.getKakaoAccount().birthday


                val profile_image: String? = result?.properties?.get("profile_image")

                userData = UserData(
                    social,
                    token,
                    name,
                    email,
                    gender,
                    age,
                    birthday,
                    profile_thum,
                    profile_image
                )
                nextActivity(userData)
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

    private fun buttonClickAnimation(button: View) =
        button.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click))

    private fun nextActivity(user: UserData) {
        val intent = Intent(applicationContext, JoinActivity::class.java)
        intent.putExtra("user", user)
        startActivity(intent)
    }
}
