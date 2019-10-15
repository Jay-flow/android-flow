package io.flow

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import io.data.UserData
import io.flow.fragments.GalleryFragment
import io.util.SetImagesNotifierInterface
import io.util.SetImagesTask
import io.util.UserSharedPreferences
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_profile.nickname
import kotlinx.android.synthetic.main.fragment_gallery.*
import kotlinx.android.synthetic.main.fragment_gallery.view.*

class ProfileActivity : AppCompatActivity() {
    private var user: UserData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        user = UserSharedPreferences(this).get()
        setupImages()
        setupProfile()
    }

    private fun setupImages() {
        val galleryFragment = supportFragmentManager.findFragmentById(R.id.upload_gallery) as GalleryFragment
        SetImagesTask(user?.images, object: SetImagesNotifierInterface {
            override fun setImagesResult(result: ArrayList<Bitmap>) {
                galleryFragment.image0.setImageBitmap(result[0])
            }
        }).execute()
    }

    private fun setupProfile() {
        introduction.setText(user?.introduction)
        nickname.setText(user?.nickname)
        if (user?.gender == "male") {
            male.isChecked = true
        } else {
            female.isChecked = true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        saveProfile()
        super.onBackPressed()
    }

    private fun saveProfile() {
        // 뒤로갈때 프로필 저장 기능 구현하기
    }
}
