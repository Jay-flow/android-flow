package io.flow.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import io.data.UserData
import io.flow.JoinActivity
import kotlinx.android.synthetic.main.fragment_input_email.view.*
import kotlinx.android.synthetic.main.fragment_input_email.view.nextButton
import kotlinx.android.synthetic.main.fragment_input_gender.*
import kotlinx.android.synthetic.main.fragment_input_gender.view.*

class GenderFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val user = arguments?.getParcelable<UserData>("user")
        val inflate = inflater.inflate(io.flow.R.layout.fragment_input_gender, container, false)

        if (user?.social == "K") {
            if (user.gender == "male") {
                inflate.male.isChecked = true
            } else {
                inflate.female.isChecked = true
            }
        }
        var genderPick = "male"
        inflate.gender.setOnCheckedChangeListener { group, checkedId ->
            val radio: RadioButton = inflate.findViewById(checkedId)
            genderPick = if (radio.text == "남자") "male" else "female"
        }
        inflate.nextButton.setOnClickListener {
            if (activity is JoinActivity) {
                val joinActivity = activity as JoinActivity
                joinActivity.setGender(genderPick)
                joinActivity.saveUserDB()
            }
        }
        return inflate
    }
}
