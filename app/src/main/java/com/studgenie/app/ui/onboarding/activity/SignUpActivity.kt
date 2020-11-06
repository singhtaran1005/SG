package com.studgenie.app.ui.onboarding.activity

import android.app.PendingIntent.getActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.msg91.sendotpandroid.library.internal.SendOTP
import com.studgenie.app.R
import com.studgenie.app.data.local.tokenDatabase.AuthViewModel
import com.studgenie.app.ui.onboarding.fragment.SignUp1Fragment
import com.studgenie.app.ui.onboarding.fragment.SignUp3Fragment
import androidx.lifecycle.Observer


class SignUpActivity : AppCompatActivity() {
    private lateinit var authViewModel: AuthViewModel
    var isTokenEmpty = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        SendOTP.initializeApp(application, "343141A0eUofjHNg5f73eff8P1")

        authViewModel = ViewModelProvider(this).get(AuthViewModel::class.java)

        authViewModel.readAllData?.observe(this, Observer{ auth->
            if (auth.isEmpty()){
                isTokenEmpty=1
                Log.d("SignUpActivity","List is empty")
                supportFragmentManager.beginTransaction()
                    .replace(R.id.signup_fragment_container, SignUp1Fragment())
                    .commit()
            }else{
                isTokenEmpty=0
                Log.d("SignUpActivity",auth[0].id.toString()+auth[0].authToken)
                supportFragmentManager.beginTransaction()
                    .replace(R.id.signup_fragment_container, SignUp3Fragment())
                    .commit()
            }
        })
    }
}