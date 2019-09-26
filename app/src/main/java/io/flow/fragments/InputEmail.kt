package io.flow.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import io.data.UserData

import io.flow.R
import kotlinx.android.synthetic.main.fragment_input_email.*
import kotlinx.android.synthetic.main.fragment_input_email.view.*


class InputEmail : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val user = arguments?.getParcelable<UserData>("user")
        val inflate = inflater.inflate(R.layout.fragment_input_email, container, false)

        inflate.email.setText(user?.email)

        return inflate
    }
}
