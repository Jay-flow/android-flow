package io.flow.fragments

import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.flow.R
import io.util.SetImagesNotifierInterface
import io.util.SetImagesTask
import io.util.UserSharedPreferences
import kotlinx.android.synthetic.main.fragment_gallery.view.*

class GalleryFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val inflate = inflater.inflate(R.layout.fragment_gallery, container, false)
        val user = activity?.let { UserSharedPreferences(it).get() }

        SetImagesTask(user?.images, object : SetImagesNotifierInterface {
            override fun setImagesResult(result: ArrayList<Bitmap>) {
                inflate.image0.setImageBitmap(result[0])
            }
        }).execute()

        return inflate
    }


}
