package io.api

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import java.io.ByteArrayOutputStream

@Suppress("SpellCheckingInspection")
class FirebaseDatabase(private val fileUploadNotifierInterface: FileUploadNotifierInterface) {
    var storage: FirebaseStorage = FirebaseStorage.getInstance()

    interface FileUploadNotifierInterface {
        fun addOnSuccessListener()
        fun addOnFailureListener()
        fun addOnCompleteListener(task :Task<Uri>)
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
        }.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> {
            return@Continuation mountainsRef.downloadUrl
        }).addOnCompleteListener {
            fileUploadNotifierInterface.addOnCompleteListener(it)
        }
    }
}