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
import android.util.Log
import android.widget.Toast
import androidx.core.content.FileProvider
import com.google.android.gms.tasks.Task
import io.api.FirebaseDatabase
import io.data.UserData
import io.util.PermissionUtil
import io.util.SetImagesNotifierInterface
import io.util.SetImagesTask
import io.util.UserSharedPreferences
import kotlinx.android.synthetic.main.activity_account.*
import java.io.File

class AccountActivity : AppCompatActivity() {
    var user: UserData? = null
    private val uploadChooser = UploadChooser()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)
        user = UserSharedPreferences(this).get()
        setName(user)
        setImage(user?.images)
        setupListener()
    }

    private fun setName(user: UserData?) {
        nickname.text = user?.nickname
    }

    private fun setImage(images: ArrayList<String>?) {
        images?.let {
            SetImagesTask(images, object : SetImagesNotifierInterface {
                override fun setImagesResult(result: ArrayList<Bitmap>) {
                    uploaded_image.setImageBitmap(result[0])
                }
            }).execute()
        }
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
            uploadChooser.addNotifier(object : UploadChooser.UploadChooserNotifierInterface {
                override fun uploadImage(bitmap: Bitmap) {
                    val filename: Int = if (user?.images == null) 0 else user!!.images!!.size

                    FirebaseDatabase(object : FirebaseDatabase.FileUploadNotifierInterface {
                        override fun addOnSuccessListener() {
                            Toast.makeText(
                                applicationContext,
                                "이미지가 성공적으로 변경되었습니다.",
                                Toast.LENGTH_LONG
                            ).show()
                            uploaded_image.setImageBitmap(bitmap)
                        }

                        override fun addOnFailureListener() {
                            Toast.makeText(
                                applicationContext,
                                "업로드에 실패했습니다. 관리자에게 문의해주세요",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                        override fun addOnCompleteListener(task: Task<Uri>) {
                            if (task.isSuccessful) {
                                val downloadUri = task.result
                                if(user?.images == null) {
                                    user!!.images = ArrayList()
                                }
                                user!!.images!!.add(downloadUri.toString())
                                UserSharedPreferences(applicationContext).set(user!!)
                                Log.d("ImagesURL", user!!.images.toString())
                            }

                        }
                    }).fileUpload(user!!.email!!, filename.toString(), bitmap)
                }
            })
            uploadChooser.show(supportFragmentManager, "")
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        uploadChooser.onRequestPermissionsResultFromCallActivity(requestCode, permissions, grantResults)
    }
}
