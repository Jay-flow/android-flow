package io.flow

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.firebase.firestore.FirebaseFirestore
import io.data.UserData
import io.util.UserSharedPreferences
import io.flow.fragments.EmailFragment
import io.flow.fragments.GenderFragment
import io.flow.fragments.NicknameFragment
import kotlinx.android.synthetic.main.activity_join.*

class JoinActivity : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()
    private lateinit var user: UserData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)
        user = intent.getParcelableExtra("user")
        val adapter = UserPagerAdapter(supportFragmentManager)
        val emailFragment = EmailFragment()
        val nicknameFragment = NicknameFragment()
        val genderFragment = GenderFragment()
        Log.d("JoinUser", user.toString())
        inputFragmentData(user, emailFragment, nicknameFragment, genderFragment)

        adapter.addItem(emailFragment)
        adapter.addItem(nicknameFragment)
        adapter.addItem(genderFragment)

        pager.adapter = adapter

        pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageSelected(position: Int) {
                progress_bar.progress = position + 1
            }

            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }
        })
    }

    fun nextItem() {
        pager.currentItem = pager.currentItem + 1
    }

    val setEmail = { email: String -> user.email = email }
    val setNickname = { nickname: String -> user.nickname = nickname }
    val setGender = { gender: String -> user.gender = gender }

    private fun inputFragmentData(
        user: Parcelable,
        emailFragment: EmailFragment,
        nicknameFragment: NicknameFragment,
        genderFragment: GenderFragment
    ) {
        val bundle = Bundle()
        bundle.putParcelable("user", user)
        emailFragment.arguments = bundle
        nicknameFragment.arguments = bundle
        genderFragment.arguments = bundle
    }

    // 파이어베이스 데이터 입력하는거 구현 해야됨 !!!!!!!!
    fun saveUserDB() {
        db.collection("users").document(user.email.toString()).set(user)
            .addOnSuccessListener {
                // 내부 SharedPreference 설정
                UserSharedPreferences(this).set(user)

                intent = Intent(this, PickActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            .addOnFailureListener {
                Toast.makeText(this, "오류가 발생했습니다. 관리자에게 문의해주세요.", Toast.LENGTH_LONG).show()
            }
    }

    @Suppress("DEPRECATION")
    class UserPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
        val items = ArrayList<Fragment>()
        fun addItem(item: Fragment) {
            items.add(item)
        }

        override fun getItem(position: Int): Fragment {
            Log.d("hi", "호출!")
            return items.get(position)
        }

        override fun getCount(): Int {
            return items.size
        }

    }
}

