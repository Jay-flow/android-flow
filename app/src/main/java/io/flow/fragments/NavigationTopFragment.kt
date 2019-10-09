package io.flow.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import io.flow.AccountActivity
import io.flow.MessageActivity
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
            goActivity(AccountActivity::class.java)
        }

        rootView.pick.setOnClickListener {
            goActivity(PickActivity::class.java)
        }

        rootView.talk.setOnClickListener {
            goActivity(MessageActivity::class.java)
        }
        return rootView
    }

    private fun goActivity(activityClass: Class<*>) {
        val intent = Intent(context, activityClass)
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        startActivity(intent)
        activity?.overridePendingTransition(0, 0)
    }
}
