package io.api

import android.graphics.Bitmap
import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream

@Suppress("SpellCheckingInspection")
class FirebaseDatabase(private val fileUploadNotifierInterface: FileUploadNotifierInterface) {
    var storage: FirebaseStorage = FirebaseStorage.getInstance()

    interface FileUploadNotifierInterface {
        fun addOnSuccessListener()
        fun addOnFailureListener()
    }

    fun fileUpload(userEmail: String, fileName: String, bitmap: Bitmap) {
        val storageRef = storage.reference
        val mountainsRef = storageRef.child("profile/${userEmail}/${fileName}")
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        val uploadTask = mountainsRef.putBytes(data)

        uploadTask.addOnFailureListener {
            fileUploadNotifierInterface.addOnFailureListener()
        }.addOnSuccessListener {
            fileUploadNotifierInterface.addOnSuccessListener()
        }
    }
}