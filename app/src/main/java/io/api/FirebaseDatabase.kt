package io.api

import android.app.Activity
import android.widget.Toast
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import io.flow.widget.ProgressDialog

class FirebaseDatabase(
    private val context: Activity
) {
    private val progressDialog = ProgressDialog(context)
    private val db = FirebaseFirestore.getInstance()

    interface DatabaseStatusNotifierInterface {
        fun addOnSuccessListener()
        fun addOnCompleteListener()
    }

    fun dataSet(
        documentsName: String,
        data: Any,
        databaseStatusNotifierInterface: DatabaseStatusNotifierInterface
    ) {
        progressDialog.show()
        db.collection("users").document(documentsName).set(data)
            .addOnSuccessListener {
                databaseStatusNotifierInterface.addOnSuccessListener()
            }
            .addOnFailureListener {
                Toast.makeText(context, "오류가 발생했습니다. 관리자에게 문의해주세요.", Toast.LENGTH_LONG).show()
            }
            .addOnCompleteListener {
                progressDialog.dismiss()
                databaseStatusNotifierInterface.addOnCompleteListener()
            }
    }

//    fun dataUpdate(
//        documentsName: String,
//        fieldName: FieldPath,
//        value: Any
//    ) {
//        db.collection("users").document(documentsName).update(fieldName, value)
//            .addOnSuccessListener {
//
//            }
//    }
}