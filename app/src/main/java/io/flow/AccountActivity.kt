package io.flow

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import io.data.UserData
import io.util.PermissionUtil
import io.util.SetImagesNotifierInterface
import io.util.SetImagesTask
import io.util.UserSharedPreferences
import kotlinx.android.synthetic.main.activity_account.*
import java.io.File

class AccountActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)
        val user = UserSharedPreferences(this).get()
        setName(user)
        setImage(user?.images)
        setupListener()
    }

    private fun setName(user: UserData?) {
        nickname.text = user?.nickname
    }

    private fun setImage (images: ArrayList<String>?) {
        SetImagesTask(images, object : SetImagesNotifierInterface {
            override fun setImagesResult(result: ArrayList<Bitmap>) {
                uploaded_image.setImageBitmap(result[0])
            }
        }).execute()
    }

    private fun setupListener() {
        profile_changed.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
        setting_changed.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
        }
        picture_changed.setOnClickListener {
            val uploadChooser = UploadChooser()
            uploadChooser.addNotifier(object: UploadChooser.UploadChooserNotifierInterface {
                override fun uploadImage(bitmap: Bitmap) {
                    uploaded_image.setImageBitmap(bitmap)
                }
            })
            uploadChooser.show(supportFragmentManager, "")
        }
    }
}
