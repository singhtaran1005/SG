package com.studgenie.app.ui.onboarding.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.studgenie.app.R



class SplashActivity : AppCompatActivity() {
    private val SPLASH_DISPLAY_LENGTH = 500
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Log.d("Splash","Inside Splash Activity")
        Handler().postDelayed(Runnable { /* Create an Intent that will start the Menu-Activity. */
            val signupActivity = Intent(this, SignUpActivity::class.java)
            this.startActivity(signupActivity)
            this.finish()
        }, SPLASH_DISPLAY_LENGTH.toLong())
    }

}