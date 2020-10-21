package com.studgenie.app.ui.onboarding.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.studgenie.app.R
import com.studgenie.app.ui.onboarding.activity.HomeActivity
import kotlinx.android.synthetic.main.fragment_sign_up_3_test.*


class SignUp3Fragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_sign_up_3_test, container, false)
        val submit_button = rootView.findViewById<Button>(R.id.submit_button)
        submit_button.setOnClickListener {
            val i = Intent(activity, HomeActivity::class.java)
            startActivity(i)
            (activity as Activity?)!!.overridePendingTransition(0, 0)
        }
        return rootView
    }
}