package com.studgenie.app.ui.onboarding.activity

import android.app.PendingIntent.getActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.msg91.sendotpandroid.library.internal.SendOTP
import com.studgenie.app.R
import com.studgenie.app.ui.onboarding.fragment.SignUp1Fragment


class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        SendOTP.initializeApp(application, "343141A0eUofjHNg5f73eff8P1")

        supportFragmentManager.beginTransaction()
            .replace(R.id.signup_fragment_container, SignUp1Fragment())
            .commit()
    }
}