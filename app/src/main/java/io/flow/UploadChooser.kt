package io.flow

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.makeramen.roundedimageview.RoundedImageView
import io.util.PermissionUtil
import kotlinx.android.synthetic.main.activity_account.*
import kotlinx.android.synthetic.main.upload_chooser.*
import java.io.File

class UploadChooser: BottomSheetDialogFragment() {
    private var CAMERA_PERMISSION_REQUEST = 1000
    private var GALLERY_PERMISSION_REQUEST = 1001
    private val FILE_NAME = "profile_picture.jpg"

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
        val photoUri = FileProvider.getUriForFile(
            activity!!.applicationContext,
            activity!!.applicationContext.packageName + ".provider",
            createCameraFile()
        )
        startActivityForResult(
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
                putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }, CAMERA_PERMISSION_REQUEST
        )
    }

    private fun createCameraFile(): File {
        val dir = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File(dir, FILE_NAME)
    }


    private fun uploadImage(imageUri: Uri) {
        val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, imageUri)
        val uploadedImage = activity?.findViewById<RoundedImageView>(R.id.uploaded_image)
        uploadedImage?.setImageBitmap(bitmap)
        this.dismiss()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            CAMERA_PERMISSION_REQUEST -> {
                if (resultCode != Activity.RESULT_OK) return
                val photoUri = FileProvider.getUriForFile(
                    activity!!.applicationContext,
                    activity!!.applicationContext.packageName + ".provider",
                    createCameraFile()
                )
                uploadImage(photoUri)
            }
            GALLERY_PERMISSION_REQUEST -> data?.let { uploadImage(it.data!!) }
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
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