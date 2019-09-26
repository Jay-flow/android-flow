package io.flow

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.renderscript.ScriptGroup
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.firebase.firestore.FirebaseFirestore
import io.common.User
import io.data.UserData
import io.flow.fragments.InputEmail
import io.flow.fragments.InputGender
import io.flow.fragments.InputName
import kotlinx.android.synthetic.main.activity_join.*
import kotlinx.android.synthetic.main.fragment_input_email.*
import java.io.Serializable

class Join : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)
        val user = intent.getParcelableExtra<Parcelable>("user")
        val adapter = UserPagerAdapter(supportFragmentManager)
        val emailFragment = InputEmail()
        val nameFragment = InputName()
        val genderFragment = InputGender()

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
    }

    private fun inputFragmentData(user: Parcelable, emailFragment: InputEmail, nameFragment: InputName, genderFragment: InputGender) {
        val bundle = Bundle()
        bundle.putParcelable("user", user)
        emailFragment.setArguments(bundle)
        nameFragment.setArguments(bundle)
        // 성별 데이터 선택되어 있는 작업 해야됨
        genderFragment.setArguments(bundle)
    }
    // 파이어베이스 데이터 입력하는거 구현 해야됨 !!!!!!!!
    private fun saveUserInfo(user: UserData) {
        db.collection("users").document(user.email.toString()).set(user)
            .addOnSuccessListener { documentReference ->
                User().save(user)
            }
            .addOnFailureListener { e -> }
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

