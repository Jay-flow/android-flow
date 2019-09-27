package io.flow.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import io.data.UserData
import io.flow.JoinActivity

import io.flow.R
import kotlinx.android.synthetic.main.fragment_input_email.view.*


class EmailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val user = arguments?.getParcelable<UserData>("user")
        val inflate = inflater.inflate(R.layout.fragment_input_email, container, false)
        inflate.email.setText(user?.email)

        inflate.nextButton.setOnClickListener {
            if(inflate.email.text.toString().isBlank()) {
                Toast.makeText(context, "이메일을 입력해주세요.", Toast.LENGTH_LONG).show()
            } else {
                if (activity is JoinActivity) {
                    val joinActivity = activity as JoinActivity
                    joinActivity.setEmail(inflate.email.text.toString())
                    joinActivity.nextItem()
                }
            }
        }
        return inflate
    }
}
