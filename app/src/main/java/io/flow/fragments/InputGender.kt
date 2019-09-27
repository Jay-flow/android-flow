package io.flow.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.facebook.FacebookSdk.getApplicationContext
import io.data.UserData
import io.flow.R
import kotlinx.android.synthetic.main.fragment_input_gender.*

class InputGender : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val user = arguments?.getParcelable<UserData>("user")
        val inflate = inflater.inflate(R.layout.fragment_input_gender, container, false)

        if(user?.social == "K") {
            if(user.gender == "male") {
               Toast.makeText(getApplicationContext(), user.gender, Toast.LENGTH_SHORT).show()
            } else {

            }
        }

        return inflate
    }
}
