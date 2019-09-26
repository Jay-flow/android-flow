package io.flow.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.data.UserData

import io.flow.R
import kotlinx.android.synthetic.main.fragment_input_name.view.*


class InputName : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val user = arguments?.getParcelable<UserData>("user")
        val inflate = inflater.inflate(R.layout.fragment_input_name, container, false)

        inflate.name.setText(user?.name)

        return inflate
    }
}
