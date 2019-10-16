package io.flow

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import io.util.PermissionUtil
import kotlinx.android.synthetic.main.upload_chooser.*
import java.io.File
import androidx.core.content.FileProvider
import java.io.IOException
import android.media.ExifInterface
import android.graphics.BitmapFactory
import android.util.Log

class UploadChooser : BottomSheetDialogFragment() {
    lateinit var imageFilePath: String
    lateinit var uploadChooserNotifierInterface: UploadChooserNotifierInterface
    lateinit var photoUri: Uri

    private var CAMERA_PERMISSION_REQUEST = 1000
    private var GALLERY_PERMISSION_REQUEST = 1001
    private val FILE_NAME = "profile_picture.jpg"

    interface UploadChooserNotifierInterface {
        fun uploadImage(bitmap: Bitmap)
    }

    fun addNotifier(listener: UploadChooserNotifierInterface) {
        uploadChooserNotifierInterface = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.upload_chooser, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupListener()
    }

    private fun setupListener() {
        upload_camera.setOnClickListener {
            checkCameraPermission()
        }
        upload_gallery.setOnClickListener {
            checkGalleryPermission()
        }
    }

    private fun checkGalleryPermission() {
        if (PermissionUtil().requestPermission(
                activity as Activity,
                GALLERY_PERMISSION_REQUEST,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        ) openGallery()
    }

    private fun checkCameraPermission() {
        if (PermissionUtil().requestPermission(
                activity as Activity,
                CAMERA_PERMISSION_REQUEST,
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        ) openCamera()
    }


    private fun openGallery() {
        val intent = Intent().apply {
            type = "image/*"
            action = Intent.ACTION_GET_CONTENT
        }
        startActivityForResult(
            Intent.createChooser(intent, "Select a photo"),
            GALLERY_PERMISSION_REQUEST
        )
    }

    private fun openCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(activity!!.packageManager) != null) {
            var photoFile: File? = null
            try {
                photoFile = createCameraFile()
            } catch (ex: IOException) {
                // Error occurred while creating the File
            }

            if (photoFile != null) {
                photoUri = FileProvider.getUriForFile(
                    activity!!.applicationContext,
                    activity!!.packageName,
                    photoFile
                )

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                startActivityForResult(takePictureIntent, CAMERA_PERMISSION_REQUEST)
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun createCameraFile(): File {
        val storageDir = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
            FILE_NAME,
            ".jpg",
            storageDir
        )
        imageFilePath = image.absolutePath
        return image
    }

    private fun exifOrientationToDegrees(exifOrientation: Int): Int {
        return when (exifOrientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90
            ExifInterface.ORIENTATION_ROTATE_180 -> 180
            ExifInterface.ORIENTATION_ROTATE_270 -> 270
            else -> 0
        }
    }

    private fun rotate(bitmap: Bitmap, degree: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(degree)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            CAMERA_PERMISSION_REQUEST -> {
                if (resultCode != Activity.RESULT_OK) return
                val bitmap = BitmapFactory.decodeFile(imageFilePath)
                var exif: ExifInterface? = null

                try {
                    exif = ExifInterface(imageFilePath)
                } catch (e: IOException) {
                    e.printStackTrace()
                }


                val exifOrientation: Int
                val exifDegree: Int

                if (exif != null) {
                    exifOrientation = exif.getAttributeInt(
                        ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_NORMAL
                    )
                    exifDegree = exifOrientationToDegrees(exifOrientation)
                } else {
                    exifDegree = 0
                }
                val bitmapRotate = rotate(bitmap, exifDegree.toFloat())
                uploadChooserNotifierInterface.uploadImage(bitmapRotate)
            }
            GALLERY_PERMISSION_REQUEST -> data?.let {
                val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, it.data!!)
                uploadChooserNotifierInterface.uploadImage(bitmap)
            }
        }
        this.dismiss()
    }

    fun onRequestPermissionsResultFromCallActivity(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            GALLERY_PERMISSION_REQUEST ->
                if (PermissionUtil().permissionGranted(
                        requestCode,
                        GALLERY_PERMISSION_REQUEST,
                        grantResults
                    )
                ) openGallery()


            CAMERA_PERMISSION_REQUEST ->
                if (PermissionUtil().permissionGranted(
                        requestCode,
                        CAMERA_PERMISSION_REQUEST,
                        grantResults
                    )
                ) openCamera()
        }
    }
}