package io.util

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import io.flow.widget.ProgressDialog
import java.io.BufferedInputStream
import java.lang.Exception
import java.net.URL

interface SetImagesNotifierInterface {
    fun setImagesResult(result: ArrayList<Bitmap>)
}

class SetImagesTask constructor(
    context: Context,
    private val images: ArrayList<String>?,
    private val setImagesNotifierInterface: SetImagesNotifierInterface
) : AsyncTask<Any, Void, ArrayList<Bitmap>?>() {
    private val progressDialog = ProgressDialog(context)

    override fun onPreExecute() {
        super.onPreExecute()
        progressDialog.show()
    }

    override fun onPostExecute(result: ArrayList<Bitmap>?) {
        result?.let {
            setImagesNotifierInterface.setImagesResult(it)
            progressDialog.dismiss()
        }
    }

    override fun doInBackground(vararg params: Any?): ArrayList<Bitmap>? {
        return try {
            val bitmaps = ArrayList<Bitmap>()
            if(images != null) {
                for(imageURL in 0 until images.size) {
                    val url = URL(images.get(imageURL))
                    val urlConnection = url.openConnection()
                    urlConnection.connect()
                    val bis = BufferedInputStream(urlConnection.getInputStream())
                    val bitmap = BitmapFactory.decodeStream(bis)
                    bis.close()
                    bitmaps.add(bitmap)
                }
            }
            return bitmaps
        } catch (e: Exception) {
            null
        }
    }
}