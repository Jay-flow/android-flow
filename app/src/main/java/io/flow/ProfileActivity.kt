package io.flow

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import io.data.UserData
import io.flow.fragments.GalleryFragment
import io.util.SetImagesNotifierInterface
import io.util.SetImagesTask
import io.util.UserSharedPreferences
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_profile.nickname
import kotlinx.android.synthetic.main.fragment_gallery.*

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
        setupImageUploadOnclick(galleryFragment)
        SetImagesTask(this, user?.images, object: SetImagesNotifierInterface {
            override fun setImagesResult(result: ArrayList<Bitmap>) {
                for (i in 0 until result.size) {
                    when(i) {
                        0 ->  galleryFragment.image0.setImageBitmap(result[0])
                        1 ->  galleryFragment.image1.setImageBitmap(result[1])
                        2 ->  galleryFragment.image2.setImageBitmap(result[2])
                        3 ->  galleryFragment.image3.setImageBitmap(result[3])
                        4 ->  galleryFragment.image4.setImageBitmap(result[4])
                        5 ->  galleryFragment.image5.setImageBitmap(result[5])
                    }
                }
            }
        }).execute()
    }

    private fun setupImageUploadOnclick(galleryFragment: GalleryFragment) {
        galleryFragment.image0.setOnClickListener {
            val uploadChooser = UploadChooser()
            uploadChooser.addNotifier(object: UploadChooser.UploadChooserNotifierInterface {
                override fun uploadImage(bitmap: Bitmap) {
                    galleryFragment.image0.setImageBitmap(bitmap)
                }
            })
            uploadChooser.show(supportFragmentManager, "")
        }
        galleryFragment.image1.setOnClickListener {
            val uploadChooser = UploadChooser()
            uploadChooser.addNotifier(object: UploadChooser.UploadChooserNotifierInterface {
                override fun uploadImage(bitmap: Bitmap) {
                    galleryFragment.image1.setImageBitmap(bitmap)
                }
            })
            uploadChooser.show(supportFragmentManager, "")
        }
        galleryFragment.image2.setOnClickListener {
            val uploadChooser = UploadChooser()
            uploadChooser.addNotifier(object: UploadChooser.UploadChooserNotifierInterface {
                override fun uploadImage(bitmap: Bitmap) {
                    galleryFragment.image2.setImageBitmap(bitmap)
                }
            })
            uploadChooser.show(supportFragmentManager, "")
        }
        galleryFragment.image3.setOnClickListener {
            val uploadChooser = UploadChooser()
            uploadChooser.addNotifier(object: UploadChooser.UploadChooserNotifierInterface {
                override fun uploadImage(bitmap: Bitmap) {
                    galleryFragment.image3.setImageBitmap(bitmap)
                }
            })
            uploadChooser.show(supportFragmentManager, "")
        }
        galleryFragment.image4.setOnClickListener {
            val uploadChooser = UploadChooser()
            uploadChooser.addNotifier(object: UploadChooser.UploadChooserNotifierInterface {
                override fun uploadImage(bitmap: Bitmap) {
                    galleryFragment.image4.setImageBitmap(bitmap)
                }
            })
            uploadChooser.show(supportFragmentManager, "")
        }
        galleryFragment.image5.setOnClickListener {
            val uploadChooser = UploadChooser()
            uploadChooser.addNotifier(object: UploadChooser.UploadChooserNotifierInterface {
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
