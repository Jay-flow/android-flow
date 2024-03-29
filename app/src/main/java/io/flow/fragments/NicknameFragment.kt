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
import kotlinx.android.synthetic.main.fragment_input_name.view.*
import kotlinx.android.synthetic.main.fragment_input_name.view.nextButton


class NicknameFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val user = arguments?.getParcelable<UserData>("user")
        val inflate = inflater.inflate(R.layout.fragment_input_name, container, false)

        inflate.name.setText(user?.nickname)
        inflate.nextButton.setOnClickListener {
            if(inflate.name.text.toString().isBlank()) {
                Toast.makeText(context, "이름을 입력해주세요.", Toast.LENGTH_LONG).show()
            } else {
                if (activity is JoinActivity) {
                    val joinActivity = activity as JoinActivity
                    joinActivity.setNickname(inflate.name.text.toString())
                    joinActivity.nextItem()
                }
            }
        }
        return inflate
    }
}
