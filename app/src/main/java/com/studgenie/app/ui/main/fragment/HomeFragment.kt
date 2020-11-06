package com.studgenie.app.ui.main.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.studgenie.app.R
import com.studgenie.app.ui.onboarding.activity.SignUpActivity
import kotlinx.android.synthetic.main.fragment_home.view.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.studgenie.app.data.local.tokenDatabase.AuthViewModel
import com.studgenie.app.data.local.userDetailsDatabase.UserViewModel

class HomeFragment : Fragment() {
    private lateinit var authViewModel: AuthViewModel
    private lateinit var userViewModel: UserViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView =inflater.inflate(R.layout.fragment_home, container, false)


        authViewModel = ViewModelProvider(requireActivity()).get(AuthViewModel::class.java)
        userViewModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)

        authViewModel.readAllData?.observe(viewLifecycleOwner, Observer{ auth->
            if (auth.isEmpty()){
                Log.d("HomeFragment1","List is empty")
            }else{
                Log.d("HomeFragment1",auth[0].id.toString()+auth[0].authToken)
            }
        })
        userViewModel.readAllData?.observe(viewLifecycleOwner, Observer{ user->
            if (user.isEmpty()){
                Log.d("HomeFragment2","List is empty")
            }else{
                Log.d("HomeFragment2",user[0].id.toString()+user[0].number)
            }
        })







        rootView.update_details_button.setOnClickListener {
            val intent = Intent(activity, SignUpActivity::class.java)
            startActivity(intent)
            (activity as Activity?)!!.overridePendingTransition(0, 0)
            activity?.finish()
        }
       return rootView
    }
}