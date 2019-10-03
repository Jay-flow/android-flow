package io.flow.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import io.flow.AccountActivity
import io.flow.PickActivity
import io.flow.R
import kotlinx.android.synthetic.main.fragment_navigation_top.*
import kotlinx.android.synthetic.main.fragment_navigation_top.view.*

class NavigationTopFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView: ViewGroup =  inflater.inflate(R.layout.fragment_navigation_top, container, false) as ViewGroup

        rootView.account.setOnClickListener {
            Toast.makeText(context, "계정설정 액티비티", Toast.LENGTH_SHORT).show()
            val intent = Intent(context, AccountActivity::class.java)
            startActivity(intent)
        }

        rootView.pick.setOnClickListener {
            Toast.makeText(context, "카드 액티비티", Toast.LENGTH_SHORT).show()
            val intent = Intent(context, PickActivity::class.java)
            startActivity(intent)
        }

        rootView.talk.setOnClickListener {
            Toast.makeText(context, "메세지 액티비티", Toast.LENGTH_SHORT).show()
        }
        return rootView
    }
}