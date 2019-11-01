package io.api

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import io.flow.widget.ProgressDialog
import java.io.ByteArrayOutputStream

@Suppress("SpellCheckingInspection")
class FirebaseStorage(
    context: Context,
    private val fileUploadNotifierInterface: FileUploadNotifierInterface
) {
    private val progressDialog = ProgressDialog(context)
    private var storage: FirebaseStorage = FirebaseStorage.getInstance()

    interface FileUploadNotifierInterface {
        fun addOnSuccessListener()
        fun addOnFailureListener()
        fun addOnCompleteListener(task: Task<Uri>)
    }

    fun fileUpload(userEmail: String, fileName: String, bitmap: Bitmap) {
        progressDialog.show()

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
            progressDialog.hide()
        }
    }
}