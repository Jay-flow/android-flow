package io.flow

import android.graphics.Bitmap
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import io.api.FirebaseDatabase
import io.data.UserData
import io.flow.fragments.GalleryFragment
import io.util.SetImagesNotifierInterface
import io.util.SetImagesTask
import io.util.UserSharedPreferences
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.fragment_gallery.*

class ProfileActivity : AppCompatActivity() {
    private var user: UserData? = null
    private var isGenderChange: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        user = UserSharedPreferences(this).get()
        user!!.images?.let {
            setupImages()
        }
        setupProfile()
    }

    private fun setupImages() {
        val galleryFragment =
            supportFragmentManager.findFragmentById(R.id.upload_gallery) as GalleryFragment
        setupImageUploadOnclick(galleryFragment)
        SetImagesTask(this, user?.images, object : SetImagesNotifierInterface {
            override fun setImagesResult(result: ArrayList<Bitmap>) {
                for (i in 0 until result.size) {
                    when (i) {
                        0 -> galleryFragment.image0.setImageBitmap(result[0])
                        1 -> galleryFragment.image1.setImageBitmap(result[1])
                        2 -> galleryFragment.image2.setImageBitmap(result[2])
                        3 -> galleryFragment.image3.setImageBitmap(result[3])
                        4 -> galleryFragment.image4.setImageBitmap(result[4])
                        5 -> galleryFragment.image5.setImageBitmap(result[5])
                    }
                }
            }
        }).execute()
    }

    private fun setupImageUploadOnclick(galleryFragment: GalleryFragment) {
        galleryFragment.image0.setOnClickListener {
            val uploadChooser = UploadChooser()
            uploadChooser.addNotifier(object : UploadChooser.UploadChooserNotifierInterface {
                override fun uploadImage(bitmap: Bitmap) {
                    galleryFragment.image0.setImageBitmap(bitmap)
                }
            })
            uploadChooser.show(supportFragmentManager, "")
        }
        galleryFragment.image1.setOnClickListener {
            val uploadChooser = UploadChooser()
            uploadChooser.addNotifier(object : UploadChooser.UploadChooserNotifierInterface {
                override fun uploadImage(bitmap: Bitmap) {
                    galleryFragment.image1.setImageBitmap(bitmap)
                }
            })
            uploadChooser.show(supportFragmentManager, "")
        }
        galleryFragment.image2.setOnClickListener {
            val uploadChooser = UploadChooser()
            uploadChooser.addNotifier(object : UploadChooser.UploadChooserNotifierInterface {
                override fun uploadImage(bitmap: Bitmap) {
                    galleryFragment.image2.setImageBitmap(bitmap)
                }
            })
            uploadChooser.show(supportFragmentManager, "")
        }
        galleryFragment.image3.setOnClickListener {
            val uploadChooser = UploadChooser()
            uploadChooser.addNotifier(object : UploadChooser.UploadChooserNotifierInterface {
                override fun uploadImage(bitmap: Bitmap) {
                    galleryFragment.image3.setImageBitmap(bitmap)
                }
            })
            uploadChooser.show(supportFragmentManager, "")
        }
        galleryFragment.image4.setOnClickListener {
            val uploadChooser = UploadChooser()
            uploadChooser.addNotifier(object : UploadChooser.UploadChooserNotifierInterface {
                override fun uploadImage(bitmap: Bitmap) {
                    galleryFragment.image4.setImageBitmap(bitmap)
                }
            })
            uploadChooser.show(supportFragmentManager, "")
        }
        galleryFragment.image5.setOnClickListener {
            val uploadChooser = UploadChooser()
            uploadChooser.addNotifier(object : UploadChooser.UploadChooserNotifierInterface {
                override fun uploadImage(bitmap: Bitmap) {
                    galleryFragment.image5.setImageBitmap(bitmap)
                }
            })
            uploadChooser.show(supportFragmentManager, "")
        }
    }

    private fun setupProfile() {
        introduction.setText(user?.introduction)
        nickname.setText(user?.nickname)
        if (user?.gender == "male") {
            male.isChecked = true
        } else {
            female.isChecked = true
        }

        val genderCheckId = gender.checkedRadioButtonId
        gender.setOnCheckedChangeListener { group, checkedId ->
            isGenderChange = genderCheckId != checkedId
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
        //super.onBackPressed()
    }

    private fun saveProfile() {
        // 뒤로갈때 프로필 저장 기능 구현하기
        val editIntroduction: String = introduction.text.toString()
        val editNickname: String = nickname.text.toString()
        val db = FirebaseDatabase(this)
        if (
            editIntroduction != user?.introduction
            || editNickname != user?.nickname
            || isGenderChange
        ) {
            if (editNickname != user?.nickname) {
                user!!.nickname = editNickname
            }
            if (editIntroduction != user?.introduction) {
                user!!.introduction = editIntroduction
            }
            if (isGenderChange) {
                val gender: String = if (male.isChecked) {
                    "male"
                } else {
                    "female"
                }
                user!!.gender = gender
            }
            val context = this
            db.dataSet(
                user!!.email.toString(),
                user!!,
                object : FirebaseDatabase.DatabaseStatusNotifierInterface {
                    override fun addOnSuccessListener() {
                        UserSharedPreferences(context).set(user!!)
                    }
                    override fun addOnCompleteListener() {
                        // 로딩 끝 -> 화면 전환 되어야 하는데 이게 안됨
                        //  [비동기 문제] 로딩 표시 때문에 이렇게해서 닫아줌, 더 좋은 방법 찾기
                        context.finish()
                    }
                })
        } else {
            this.finish()
        }
    }
}
