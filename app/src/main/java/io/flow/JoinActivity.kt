package io.flow

import android.annotation.SuppressLint
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
import io.common.User
import io.data.UserData
import io.db.UserSharedPreferences
import io.flow.fragments.EmailFragment
import io.flow.fragments.GenderFragment
import io.flow.fragments.NameFragment
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
        val nameFragment = NameFragment()
        val genderFragment = GenderFragment()
        Log.d("JoinUser", user.toString())
        inputFragmentData(user, emailFragment, nameFragment, genderFragment)

        adapter.addItem(emailFragment)
        adapter.addItem(nameFragment)
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

        UserSharedPreferences(this).set(user)

    }

    fun nextItem() {
        pager.currentItem = pager.currentItem + 1
    }


    val setEmail = { email: String -> user.email = email }
    val setName = { name: String -> user.name = name }
    val setGender = { gender: String -> user.gender = gender }



    private fun inputFragmentData(
        user: Parcelable,
        emailFragment: EmailFragment,
        nameFragment: NameFragment,
        genderFragment: GenderFragment
    ) {
        val bundle = Bundle()
        bundle.putParcelable("user", user)
        emailFragment.arguments = bundle
        nameFragment.arguments = bundle
        // 성별 데이터 선택되어 있는 작업 해야됨
        genderFragment.arguments = bundle
    }

    // 파이어베이스 데이터 입력하는거 구현 해야됨 !!!!!!!!
    fun saveUserDB() {
        db.collection("users").document(user.email.toString()).set(user)
            .addOnSuccessListener { documentReference ->
                intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "오류가 발생했습니다. 관리자에게 문의해주세요.", Toast.LENGTH_LONG).show()
            }
    }

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
